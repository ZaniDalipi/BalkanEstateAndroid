# BalkanEstate Android Development Rules

## Architecture: MVI (Model-View-Intent)

Always when developing any screen think on MVI architecture with the following structure.

### Dependency Injection: Koin
- Use one DI file for each layer → `data(DI)`, `presentation(DI)`

### Module Structure
```
module/
├── data/           # The HOW - implementation
├── domain/         # The WHAT - interfaces (pure Kotlin)
└── presentation/   # UI layer
```

---

## Data Layer - The HOW

Implementation of repository, requests, DI, routes, etc.

```kotlin
class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository { }
```

---

## Domain Layer - The WHAT

What to implement - we want to keep this **pure Kotlin** (no Android dependencies).

```kotlin
interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}
```

---

## Presentation Layer

In presentation we define: **Actions**, **State**, **Events**, **Screen**, and **ViewModel**

### Action
What the user can do in the screen - **from the Screen to the ViewModel**

```kotlin
sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnLoginClick: LoginAction
    data object OnRegisterClick: LoginAction
}
```

### Event
One-time events sent **from the ViewModel to the UI**

```kotlin
sealed interface LoginEvent {
    data class Error(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}
```

### State
The state of an object that we use in the UI to track its changes and react to different actions

```kotlin
data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false
)
```

### Screen and ScreenRoot
This is how we glue everything together and how we interact with each component in the app

```kotlin
@Composable
fun ScreenRoot(
    viewModel: LoginViewModel = koinViewModel()
) {
    Screen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Screen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    // UI implementation
}

@Preview
@Composable
private fun ScreenPreview() {
    BalkanEstateTheme {
        Screen(
            state = LoginState(),
            onAction = {}
        )
    }
}
```

---

## File Organization

Each feature module should follow this structure:

```
feature/
├── data/
│   └── src/main/java/com/zanoapps/feature/data/
│       ├── di/
│       │   └── FeatureDataModule.kt
│       └── repository/
│           └── FeatureRepositoryImpl.kt
├── domain/
│   └── src/main/java/com/zanoapps/feature/domain/
│       ├── model/
│       │   └── FeatureModel.kt
│       └── repository/
│           └── FeatureRepository.kt
└── presentation/
    └── src/main/java/com/zanoapps/feature/presentation/
        ├── di/
        │   └── FeaturePresentationModule.kt
        └── feature_screen/
            ├── FeatureAction.kt
            ├── FeatureEvent.kt
            ├── FeatureState.kt
            ├── FeatureScreen.kt
            └── FeatureViewModel.kt
```

---

## Naming Conventions

- **Actions**: `On[ActionName]` (e.g., `OnLoginClick`, `OnTogglePasswordVisibility`)
- **Events**: Descriptive names (e.g., `LoginSuccess`, `Error`)
- **States**: `[Feature]State` (e.g., `LoginState`, `SearchState`)
- **ViewModels**: `[Feature]ViewModel` (e.g., `LoginViewModel`, `SearchViewModel`)
- **Screens**: `[Feature]Screen` and `[Feature]ScreenRoot` (e.g., `LoginScreen`, `LoginScreenRoot`)

---

## Best Practices

1. **Keep domain pure**: No Android dependencies in the domain layer
2. **Single responsibility**: Each class/function should have one purpose
3. **State hoisting**: Keep state in ViewModel, not in Composables
4. **Immutable state**: Use `copy()` to update state, never mutate
5. **Use sealed interfaces**: For Actions and Events for exhaustive when expressions
6. **Preview functions**: Always include Preview composables for each screen
7. **DI modules**: Separate DI modules for data and presentation layers
