# MTGScanner

Checklist
- [ ] Verify minimum requirements (Android SDK, Kotlin, Gradle)
- [ ] Open the project in Android Studio
- [ ] Run on a device/emulator with a camera

Description
-----------
MTGScanner is an Android application for scanning Magic: The Gathering cards and extracting relevant information (name, set, images, etc.). It is designed as a modular, maintainable foundation for experimentation and improvements: image-based card recognition, public database lookups, and a modern UI built with Jetpack Compose.

Key features
- Card scanning using device camera (CameraX)
- Modern UI built with Jetpack Compose
- Architecture based on MVVM and Clean Code principles
- Reactive state management with StateFlow
- Image loading and caching with Coil
- Network communication with Retrofit

Architecture
-----------
The project follows patterns and principles that promote maintainability, testability, and evolvability:

- MVVM (Model-View-ViewModel):
  - View (Jetpack Compose): declarative UI components.
  - ViewModel: exposes StateFlow/SharedFlow for UI state and events, handles presentation logic.
  - Model / Domain: entities, use-cases, and repository interfaces.

- Clean Architecture (layered separation):
  - Presentation: Compose + ViewModels. Depends only on contracts (interfaces) for use-cases.
  - Domain: pure business logic (use-cases, models). Framework-agnostic.
  - Data: repository implementations, remote sources (Retrofit) and local sources (Room/cache), mappers and network models.

- Applied principles:
  - Single Responsibility
  - Dependency Inversion (DI for easier testing)
  - Separation of Concerns

Technologies & Libraries
-----------------------
- Kotlin
- Jetpack Compose (UI)
- CameraX (image capture and processing)
- Retrofit (HTTP / API client)
- Coil (image loading and caching in Compose)
- StateFlow / Coroutines (reactive state and concurrency)
- (Optional) Room / DataStore for local cache/persistence
- (Optional) TensorFlow Lite / ML Kit / OCR for on-device recognition

Project structure (summary)
---------------------------
The structure follows a modular folder layout commonly used in modern Android apps:

- `app/`: main Android module
  - `src/main/java/.../presentation` -> Composables, ViewModels
  - `src/main/java/.../domain` -> Models, interfaces, use-cases
  - `src/main/java/.../data` -> Repositories, data sources, Retrofit services
  - `build.gradle.kts` -> module-specific build configuration

Quick start
-----------
Prerequisites
- JDK 11 or newer
- Android Studio (recommended: Arctic Fox or newer)
- Android SDK and platform tools installed
- An Android device or emulator with camera support (or a virtual camera)

Run the app
1. Clone the repository:

   git clone https://github.com/gabrielnapolespe/mtg-scanner-android-app.git

2. Open the project in Android Studio:
   - File -> Open -> select the `MTGScanner` folder

3. Sync Gradle if prompted by Android Studio.

4. Connect a device or start an emulator with camera support.

5. Run the app from Android Studio (Run > app).

Permissions
-----------
The app requires runtime camera permission. Make sure the Compose UI or hosting Activity requests permission before starting the capture flow.

Implementation notes
--------------------
- StateFlow: ViewModels should expose UIState / UIEvent to the UI. Keep business logic out of the Presentation layer.
- Repositories: expose contracts in the Domain layer and implement adapters in the Data layer to allow easy mocking for tests.
- CameraX: prefer `ImageAnalysis` for frame-by-frame processing and delegate preprocessing to a UseCase or an analysis layer.

Contributing
------------
Contributions are welcome. Open issues to discuss major changes and keep commits small with conventional messages.

Contact
-------
For questions or collaboration, open an issue or contact the repository maintainer.

Roadmap / suggestions
---------------------
- Integrate a card recognition layer using an external service (e.g., Scryfall API) or an on-device TFLite model.
- Tests: unit tests for UseCases and ViewModels; instrumentation tests for camera flows and UI.
- CI: add GitHub Actions for linting, build, and tests.
