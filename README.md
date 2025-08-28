# Vehicle Manager - Android App

A modern Android application for vehicle management that enables users to create, view, and manage their vehicles with dynamic feature discovery. Built with Jetpack Compose, Clean Architecture, and MVI pattern.

## 📱 Features

### Core Functionality
- **Vehicle Creation Flow**: Intuitive multi-step process for adding vehicles
  - Brand selection from major automobile manufacturers
  - Series selection based on chosen brand
  - Build year selection with comprehensive coverage
  - Fuel type selection (Diesel, Gasoline, Hybrid, Electric)
  - Search functionality for quick filtering

- **Vehicle Management**: Complete vehicle lifecycle management
  - List all user vehicles with detailed information
  - Select active vehicle with visual feedback
  - Delete vehicles with business logic validation
  - Persistent storage with cross-session data retention

- **Dynamic Dashboard**: Adaptive interface based on user state
  - Empty state with vehicle creation prompt
  - Selected vehicle display with detailed information
  - Dynamic feature list based on vehicle brand/model
  - Seamless navigation to vehicle management

### Technical Features
- **Modern UI**: Material 3 design with custom gradients and animations
- **Responsive Design**: Optimized for various screen sizes
- **Offline First**: Local database with Room for reliable data access
- **Performance Optimized**: LazyColumn for efficient list rendering
- **Accessibility Ready**: Content descriptions and semantic properties

## 🏗️ Architecture

### MVI (Model-View-Intent) Pattern
The application follows MVI architecture pattern for predictable state management:

```
User Intent (Actions) → ViewModel (Process) → State Updates → UI Recomposition
```

**Key Components:**
- **Actions**: Sealed interfaces representing user intents (`MyCarsListAction`, `CreateVehicleAction`)
- **States**: Immutable data classes holding UI state (`MyCarsListState`, `CreateVehicleState`)  
- **Events**: One-time navigation/side effects (`MyCarsListEvent`, `CreateVehicleEvent`)
- **ViewModels**: Business logic processors with state management

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│            Presentation             │ ← UI Layer (Compose, ViewModels)
├─────────────────────────────────────┤
│              Domain                 │ ← Business Logic (Use Cases, Models)
├─────────────────────────────────────┤
│               Data                  │ ← Data Sources (Room, Repositories)
└─────────────────────────────────────┘
```

### Multi-Module Structure
- **`:app`** - Application module with navigation and DI setup
- **`:vehicles:presentation`** - UI layer with Composables and ViewModels
- **`:vehicles:domain`** - Business logic and use cases
- **`:vehicles:data`** - Data layer with Room database and repositories

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin** - Primary development language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Coroutines & Flow** - Asynchronous programming and reactive data streams
- **Hilt** - Dependency injection framework

### Architecture Components
- **ViewModel** - UI-related data holder with lifecycle awareness
- **StateFlow/SharedFlow** - State management and event handling
- **Navigation Component** - Type-safe navigation between screens
- **Room** - Local database for persistent storage

### UI & UX
- **Material 3** - Latest Material Design components
- **Custom Animations** - Smooth transitions and state changes
- **Gradient Backgrounds** - Enhanced visual appeal
- **Splash Screen API** - Professional app launch experience

### Testing
- **JUnit 4** - Unit testing framework  
- **MockK** - Modern Kotlin mocking framework with Android support
- **Coroutines Test** - Testing utilities for async code
- **Compose UI Testing** - Automated UI interaction testing
- **Hilt Testing** - Dependency injection testing support

## 🚀 Getting Started

### Prerequisites
- **Android Studio** - Latest stable version (Recommended: Hedgehog or newer)
- **JDK** - Version 17 or higher
- **Android SDK** - API level 21+ (Android 5.0)
- **Gradle** - Version 8.0+ (handled by wrapper)

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone [repository-url]
   cd VehicleManager
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

3. **Build the Project**
   ```bash
   ./gradlew clean build
   ```

4. **Run the Application**
   - Connect Android device or start emulator (API 21+)
   - Click "Run" in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

### Build Variants
- **Debug** - Development build with debugging enabled
- **Release** - Production build with optimizations enabled

## 📖 Usage Guide

### Creating Your First Vehicle

1. **Launch Application**: Open the Vehicle Manager app
2. **Add Vehicle**: Tap the "Add Vehicle" button on dashboard
3. **Select Brand**: Choose from BMW, Mercedes, Audi, or Volkswagen
4. **Choose Series**: Pick a series based on your selected brand
5. **Set Year**: Select the build year of your vehicle
6. **Pick Fuel Type**: Choose the appropriate fuel type
7. **Complete Creation**: Vehicle is automatically created and selected

### Managing Vehicles

1. **View All Vehicles**: Tap the menu icon on dashboard to see vehicle list
2. **Select Vehicle**: Tap any vehicle to make it the active selection
3. **Delete Vehicle**: Use the delete icon (unavailable for selected vehicle)
4. **Return to Dashboard**: Use back navigation or select a vehicle

### Understanding Features

Each vehicle displays relevant features based on its brand:
- **Diagnostics** - Vehicle diagnostic capabilities
- **Live Data** - Real-time vehicle data monitoring  
- **Battery Check** - Battery health and status
- **Car Check** - Comprehensive vehicle assessment

## 🧪 Testing

### Running Tests

**Unit Tests**
```bash
./gradlew test
```

**Instrumented Tests**
```bash
./gradlew connectedAndroidTest
```

**Specific Module Tests**
```bash
./gradlew :vehicles:presentation:test
./gradlew :vehicles:data:test
```

### Test Coverage
- **ViewModel Tests** - Complete business logic coverage with MockK
- **Repository Tests** - Data layer operations and Room database
- **Use Case Tests** - Domain layer validation and business rules
- **UI Integration Tests** - End-to-end user interaction flows with Compose UI Testing
- **Android Instrumentation Tests** - Real device/emulator testing with Hilt integration

## 🔧 Development

### Project Structure
```
VehicleManager/
├── app/                              # Application module
│   ├── src/main/                     # Main source code
│   └── src/test/                     # Unit tests
├── vehicles/                         # Feature modules
│   ├── data/                         # Data layer
│   │   ├── database/                 # Room database
│   │   ├── repo/                     # Repository implementations
│   │   └── datasource/               # Data sources
│   ├── domain/                       # Domain layer
│   │   ├── model/                    # Domain models
│   │   ├── repo/                     # Repository interfaces
│   │   ├── usecase/                  # Business use cases
│   │   └── util/                     # Domain utilities
│   └── presentation/                 # Presentation layer
│       ├── ui/                       # Compose UI screens
│       ├── components/               # Reusable UI components
│       └── theme/                    # App theming
├── build-logic/                      # Build configuration
└── gradle/                           # Gradle wrapper
```

### Key Design Decisions

**MVI over MVVM**
- Unidirectional data flow for predictable state management
- Better handling of complex UI states
- Easier testing and debugging

**Multi-Module Architecture**
- Clear separation of concerns
- Improved build times with parallel compilation
- Enhanced code reusability

**Room Database**
- Offline-first approach for reliability
- Type-safe database operations
- Automatic data seeding for better UX

### Adding New Features

1. **Define Domain Models** - Add models in `:vehicles:domain`
2. **Create Use Cases** - Implement business logic
3. **Update Repository** - Add data operations in `:vehicles:data`
4. **Build UI** - Create Composables in `:vehicles:presentation`
5. **Add Tests** - Comprehensive test coverage
6. **Update Documentation** - Keep README current

## 🐛 Troubleshooting

### Common Issues

**Build Fails**
- Ensure JDK 17+ is installed and configured
- Clean and rebuild: `./gradlew clean build`
- Invalidate caches in Android Studio

**App Crashes on Launch**
- Check device API level (minimum API 21)
- Verify all dependencies are properly injected
- Check Logcat for detailed error messages

**Database Issues**
- Clear app data if schema changes cause conflicts
- Verify Room database version increments

**UI Not Responding**
- Ensure state updates are on main thread
- Check for infinite recomposition loops
- Verify proper lifecycle handling

## 📄 License

This project is created as a job assignment demonstration for Carly Automotive.

## 🤝 Contributing

This is a job assignment project. For questions or clarifications, please contact the development team.

## 📞 Support

For technical support or questions:
- Review this documentation
- Check the troubleshooting section
- Examine unit tests for usage examples
- Contact: [mailto:ghiath.mutlak@gmail.com]

---

**Built with ❤️ using modern Android development practices**