# Missing Deliverables
- A working APK file - Mandatory ✅: A compiled APK we can install and test.
- A video demo - Mandatory ✅: A short video showcasing all the flows of your app.

Soka Singing Application

A utility music application for BSG(Bharat Soka Gakkai) Users. 
Description: This is an Android application that fetches songs and its details like lyrics and playable link, which the users can access to listen the Soka Gakkai songs along with their lyrics in their respective meetings. 

Features Implemented:
1.	Songs Listing: Fetches and displays a list of Songs from Firebase Realtime Database.
2.	Songs Details: Allows users to view detailed information about a selected song, such as: lyrics and also play the song using the embedded music player
3.	Tools and Libraries Used: App follows clean MVVM architecture with proper segregation in data, domain and presentation layer. Android features like Jetpack Compose (for UI), Coroutines with flow for async tasks, Hilt for dependency injection, Navigation compose for navigation have been used. 
4.	App uses Firebase’s data caching functionality for offline usage. 
5.	Language: Kotlin
6.	Minimum SDK: API 26(Android 8.0)

Known Limitations:
1.	Song playback not available right now in offline mode.
2.	Language Support: Currently, the app only supports English.
3.	Limited Test Coverage: The app has not been extensively tested across all Android devices or OS versions.
Installation:
1.	Clone the repository:
2.	Open the project in Android Studio.
3.	Build the project to download dependencies.
4.	Run the app on an emulator or physical device.


Future Improvements:
1.	Add user specific experience for the members, by adding account creation options. Allowing users to save their favorite songs for easy access. 
2.	UI Enhancements: Improve UI/UX for a better user experience, including bottom navigation and settings functionalities. 

