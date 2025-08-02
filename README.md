# 🕌 IMCA Prayer App

A complete prayer times application with a Spring Boot backend API and Android mobile app for the Indianapolis Muslim Community Association (IMCA).

## 📱 Features

### Android App
- **Prayer Times**: Real-time prayer times with current prayer highlighting
- **Qibla Direction**: Compass-based Qibla direction finder
- **Quran**: Complete Surah list with Arabic text
- **Settings**: App customization and IMCA information
- **Smart Current Prayer**: Automatically determines current prayer based on time
- **Modern UI**: Dark theme with IMCA branding and centered content
- **API Integration**: Connects to backend for accurate prayer times

### Backend API
- **Prayer Times API**: RESTful endpoints for prayer times
- **Date-based Queries**: Get prayer times for specific dates
- **IMCA Configuration**: Customized for Indianapolis location
- **12-hour Format**: AM/PM time format support
- **Deployment Ready**: Configured for Render deployment

## 🏗️ Architecture

```
PrayerTimesApp/
├── src/                    # Spring Boot Backend API
│   ├── main/java/
│   └── resources/
├── PrayerAppAndroid/       # Android Mobile App
│   ├── app/src/main/java/com/masjid/prayerapp/
│   │   ├── MainActivity.kt          # Main app entry point
│   │   ├── PrayerListScreen.kt      # Prayer times display
│   │   ├── QiblaScreen.kt           # Qibla direction
│   │   ├── QuranScreen.kt           # Quran Surah list
│   │   ├── SettingsScreen.kt        # App settings
│   │   ├── PrayerViewModel.kt       # Data management
│   │   ├── PrayerApiService.kt      # API communication
│   │   └── Models.kt                # Data models
│   └── app/build/outputs/apk/debug/app-debug.apk
├── render.yaml            # Render deployment config
├── Dockerfile             # Container configuration
└── pom.xml               # Maven dependencies
```

## 🚀 Quick Start

### Backend API

1. **Clone the repository**
   ```bash
   git clone https://github.com/muhammadahmadr1zwan/PrayerTimesApp.git
   cd PrayerTimesApp
   ```

2. **Run the backend**
   ```bash
   mvn spring-boot:run
   ```

3. **API Endpoints**
   - `GET /api/prayer-times/today` - Get today's prayer times
   - `GET /api/prayer-times/tomorrow` - Get tomorrow's prayer times
   - `GET /api/prayer-times/{date}` - Get prayer times for specific date

### Android App

1. **Open in Android Studio**
   ```bash
   cd PrayerAppAndroid
   # Open Android Studio and import the project
   ```

2. **Build the APK**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install on device**
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`
   - Enable "Install from unknown sources" on your device
   - Install the APK

## 📋 API Response Format

```json
{
  "date": "2025-08-02",
  "prayers": [
    {
      "name": "Fajr",
      "athan": "05:40:00 AM",
      "iqamah": "06:00:00 AM"
    },
    {
      "name": "Sunrise",
      "athan": "06:52:00 AM",
      "iqamah": "06:52:00 AM"
    },
    {
      "name": "Dhuhr",
      "athan": "12:00:00 PM",
      "iqamah": "12:20:00 PM"
    },
    {
      "name": "Asr",
      "athan": "03:45:00 PM",
      "iqamah": "04:05:00 PM"
    },
    {
      "name": "Maghrib",
      "athan": "05:08:00 PM",
      "iqamah": "05:13:00 PM"
    },
    {
      "name": "Isha",
      "athan": "06:38:00 PM",
      "iqamah": "06:58:00 PM"
    }
  ]
}
```

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot** - Web framework
- **Maven** - Build tool
- **Docker** - Containerization
- **Render** - Deployment platform

### Android App
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI components
- **OkHttp** - HTTP client
- **Moshi** - JSON serialization
- **Coroutines** - Asynchronous programming
- **ViewModel** - Architecture component

## 📱 App Screenshots

### Prayer Times Screen
- IMCA header with current date
- Current prayer highlighting
- All prayer times with Athan and Iqamah
- Centered content layout

### Qibla Direction Screen
- Interactive compass display
- Location information
- Distance to Kaaba
- Accuracy indicators

### Quran Screen
- Complete Surah list
- Arabic text support
- Last read tracking
- Play functionality

### Settings Screen
- Prayer notifications
- Location settings
- App customization
- IMCA contact information

## 🔧 Configuration

### Backend Configuration
- **Location**: Indianapolis, IN
- **Time Zone**: Eastern Time
- **Prayer Calculation**: Custom algorithm
- **Iqamah Delays**: IMCA-specific timing

### Android Configuration
- **API Base URL**: `https://prayer-app-backend-vozu.onrender.com/api/prayer-times/`
- **Network Timeout**: 30 seconds
- **Theme**: Dark mode
- **Language**: English

## 🚀 Deployment

### Backend Deployment
The backend is automatically deployed on Render:
- **URL**: https://prayer-app-backend-vozu.onrender.com
- **Health Check**: `/api/prayer-times/today`
- **Auto-deploy**: On push to main branch

### Android Deployment
- Build APK using Android Studio or Gradle
- Test on physical device
- Distribute APK to users

## 📞 Contact Information

**Indianapolis Muslim Community Association (IMCA)**
- **Address**: 2846 Cold Spring Rd, Indianapolis, IN 46222
- **Phone**: (317) 855-9934
- **Website**: [IMCA Website]

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is developed for the Indianapolis Muslim Community Association (IMCA).

## 🙏 Acknowledgments

- IMCA community for prayer time requirements
- Spring Boot and Android development communities
- Open source contributors

---

**Made with ❤️ for the Muslim community** 