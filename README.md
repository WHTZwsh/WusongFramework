# 雾凇框架 · Wusong Framework

一套「轻量、可读、可拆」的 Android 应用脚手架：Kotlin + Jetpack Compose，只做架构骨架 + 数据层 + UI 组件库，不塞重型三方库（不引 Koin / Hilt / Paging3）。

## 模块一览

| 模块 | 职责 | 关键类 |
|---|---|---|
| `:wusong-core` | 基础工具，无 Android 重依赖 | `Outcome`、`WsError`、`WsDI`、`WsLog` |
| `:wusong-arch` | MVI 契约与状态容器 | `UiState/UiIntent/UiEffect`、`WusongViewModel`、`PageState`、`PagingController` |
| `:wusong-network` | Retrofit + OkHttp 封装 | `RimeHttp`、`RimeHttpConfig`、`safeRequest`、拦截器 |
| `:wusong-ui` | Compose 组件库与主题 | `RimeTheme`、`WsStateHost`、`WsPagingLazyColumn`、`WsTopAppBar`、`WsCard`、`WsButton`、`WsAsyncImage` |
| `:wusong-router` | Navigation 封装 | `RimeNavHost`、`RimeNav`、`navigateOnce` |
| `:app` | Demo（JSONPlaceholder 列表 + 详情） | `WusongApp`、`HomeScreen`、`DetailScreen` |

依赖方向：`app → router → ui → arch → core`，`network → core`，互不反向依赖。

## 技术栈

Kotlin 2.0.21 · AGP 8.7.3 · Compose BOM 2024.12.01 · Material3 · Navigation 2.8.5 · Lifecycle 2.8.7 · Retrofit 2.11.0 + OkHttp 4.12.0 · kotlinx-serialization 1.7.3 · Coil 2.7.0 · Coroutines 1.9.0

compileSdk 35 / minSdk 24 / targetSdk 35 / jvmTarget 17

## 打开工程

1. Android Studio（Koala 及以上）+ **JDK 17** 打开根目录 `WusongFramework`。
2. 首次同步会下载 Gradle 8.9 分发包；若网络受限，先在能联网的环境执行 `gradle wrapper --gradle-version 8.9` 生成 `gradlew` 与 `gradle-wrapper.jar`，或在 Studio 中指向本地 Gradle。
3. 运行 `:app`，首页拉 `posts` 列表，点卡片进详情（Demo 用公开接口 `jsonplaceholder.typicode.com`，需联网）。

## 用法速查

**依赖注入（自研 WsDI）**

```kotlin
WsDI.single<PlaceholderApi> { RimeHttp.create() }
WsDI.single { PostRepository(WsDI.get()) }
val repo: PostRepository = WsDI.get()          // 未注册则抛异常
```

**网络请求（Outcome 统一结果）**

```kotlin
RimeHttp.install(RimeHttpConfig(baseUrl = "https://api.xxx.com/", debug = true))
val api = RimeHttp.create<PlaceholderApi>()

suspend fun fetchPosts(page: Int): Outcome<List<Post>> =
    safeRequest { api.getPosts((page - 1) * 20, 20) }   // 异常自动翻译为 WsError
```

**MVI 页面（WusongViewModel）**

```kotlin
class HomeViewModel : WusongViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {
    override fun reduce(state: HomeState, intent: HomeIntent): HomeState = when (intent) { ... }
}
// UI 侧
val state = vm.uiState.collectAsUiState()
vm.uiEffect.collectEffect { effect -> ... }
```

**分页（PagingController）**

```kotlin
val paging = PagingController<Post> { page -> repo.fetchPosts(page) }
paging.refresh(viewModelScope)     // 下拉刷新
WsPagingLazyColumn(controller = paging) { _, item -> PostCard(item) }   // 自动续拉
```

**状态页 / 路由**

```kotlin
WsStateHost(state = state.page, onRetry = { dispatch(Retry) }) { data -> ... }
RimeNavHost(startDestination = AppRoutes.HOME) { composable(AppRoutes.HOME) { HomeRoute() } }
navController.navigateOnce(AppRoutes.detail(id))   // 单例 + 300ms 防抖
```

## Demo 说明

- 首页：`WsTopAppBar` + `WsPagingLazyColumn`，下拉刷新、自动续拉、错误 toast。
- 详情：`WsStateHost` 承载 loading / error / empty，失败可重试。
- 数据：JSONPlaceholder `/posts`，`_start` + `_limit` 分页，每页 20 条。

## 可继续扩展

- `RimeHttpConfig.businessCodePolicy`：按后端 envelope 统一校验业务码（当前 Demo 用 `None`）。
- 本地缓存层（Room / DataStore）挂在 Repository 之下。
- `WsDI` 加作用域与 `factory`；`PagingController` 加预加载阈值配置。
- 组件库补 `WsDialog`、`WsTabRow`、`WsSearchBar`，主题补深色模式与动态取色。
