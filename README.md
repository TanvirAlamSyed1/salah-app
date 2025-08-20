# Salah Times App 🕌

A minimalist and modern Android application built with Jetpack Compose that displays daily prayer (Salah) times based on the user's current location.
<img width="400" height="900" alt="image" src="https://github.com/user-attachments/assets/0dfadc00-c047-4c34-976c-7bc17c2fd532" />


## ✨ Features

-   **📍 Location-Based Times**: Automatically detects the user's location to provide accurate, local prayer times.
-   **⏳ Live Countdown**: A dynamic header shows the next prayer and a real-time countdown.
-   **📖 Expandable Details**: Each prayer time card can be tapped to expand and view a description of the prayer and its benefits.
-   **🎨 Sleek Design**: A clean, aesthetic UI with custom gradient backgrounds for each prayer card and a subtle, app-wide gradient background.
-   **🔄 Offline Capability (WIP)**: Built with a repository pattern that can be extended to support offline data caching.

## 🛠️ Technology Stack

This project is a showcase of modern Android development practices.

-   **UI**: 100% Jetpack Compose for a declarative, modern UI.
-   **Architecture**: Model-View-ViewModel (MVVM) for a clean separation of concerns.
-   **Asynchronous Programming**: Kotlin Coroutines and Flow for managing background tasks and state updates.
-   **Networking**: Retrofit and Moshi for fetching and parsing data from a remote API.
-   **Dependency Injection**: Manually implemented via a ViewModel Factory to create a testable and decoupled codebase.
-   **Location**: Google Play Services for fetching the device's location.

## 🌐 API Used

This project uses the free and public [Al-Adhan API](https://aladhan.com/prayer-times-api) for all prayer time data.

## 🚀 Setup and Installation

1.  Clone the repository:
    ```bash
    git clone [https://github.com/TanvirAlamSyed1/salah-app.git](https://github.com/TanvirAlamSyed1/salah-app.git)
    ```
2.  Open the project in the latest stable version of Android Studio.
3.  Build and run the app on an emulator or a physical device. (Location services work best on a physical device or an emulator with Google Play Services).

##  roadmap Future Goals

-   Implement a Qibla compass feature.
-   Add a prayer tracking screen.
-   Set up notifications for upcoming prayer times.
-   Integrate a dependency injection framework like Hilt.
