# 🕌 IMCA Prayer App

A complete prayer times application with a Spring Boot backend API and Android mobile app for the Indianapolis Muslim Community Association (IMCA).

## 📱 Download App

### QR Code Download
Scan this QR code to download the APK directly to your Android device:

![IMCA Prayer App QR Code](imca_prayer_app_qr.png)

**Download Link**: [https://muhammadahmadr1zwan.github.io/PrayerTimesApp/download.html](https://muhammadahmadr1zwan.github.io/PrayerTimesApp/download.html)

### Manual Download
- **APK File**: `PrayerAppAndroid/app/build/outputs/apk/debug/app-debug.apk`
- **Size**: ~16MB
- **Android Version**: 6.0 (API 23) and above

## 📱 Features

### Android App - **IMPLEMENTED FEATURES** ✅
- **Prayer Times**: Real-time prayer times with current prayer highlighting ✅
- **Smart Current Prayer**: Automatically determines current prayer based on time ✅
- **Modern UI**: Dark theme with IMCA branding and centered content ✅
- **API Integration**: Connects to backend for accurate prayer times ✅
- **Bottom Navigation**: 5-tab navigation (Home, Prayers, Dhikr, Qibla, Settings) ✅
- **Qibla Direction**: Real compass integration with GPS location and Qibla calculation ✅
- **Hijri Date**: Accurate Hijri calendar date display ✅
- **Prayer Notifications**: Real-time notifications before prayer times ✅
- **Location Services**: GPS integration for accurate Qibla direction ✅
- **Settings**: Functional settings with working notifications and preferences ✅

### Backend API - **FULLY IMPLEMENTED** ✅
- **Prayer Times API**: RESTful endpoints for prayer times ✅
- **Date-based Queries**: Get prayer times for specific dates ✅
- **IMCA Configuration**: Customized for Indianapolis location ✅
- **12-hour Format**: AM/PM time format support ✅
- **Deployment Ready**: Configured for Render deployment ✅

## 🏗️ Architecture

```
PrayerTimesApp/
├── src/                    # Spring Boot Backend API
│   └── main/java/com/masjid/prayerapp/
│       ├── PrayerController.java      # REST endpoints
│       ├── PrayerService.java         # Business logic
│       ├── PrayerCalculationService.java # Prayer time calculations
│       ├── Prayer.java                # Data model
│       └── PrayerTimeResponse.java    # API response model
├── PrayerAppAndroid/       # Android Mobile App
│   ├── app/src/main/java/com/masjid/prayerapp/
│   │   ├── MainActivity.kt          # Main app entry point
│   │   ├── PrayerListScreen.kt      # Prayer times display ✅
│   │   ├── QiblaScreen.kt           # Real compass & GPS integration ✅
│   │   ├── SettingsScreen.kt        # Functional settings ✅
│   │   ├── PrayerTimesViewModel.kt  # Data management ✅
│   │   ├── PrayerApiService.kt      # API communication ✅
│   │   ├── PrayerNotificationService.kt # Notification system ✅
│   │   └── Models.kt                # Data models ✅
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
   - `GET /api/prayer-times/today` - Get today's prayer times ✅
   - `GET /api/prayer-times/tomorrow` - Get tomorrow's prayer times ✅
   - `GET /api/prayer-times/{date}` - Get prayer times for specific date ✅

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
- **StateFlow** - Reactive state management
- **AlarmManager** - Notification scheduling
- **Location Services** - GPS integration
- **Sensor Framework** - Compass functionality

## 📱 App Screenshots

### Prayer Times Screen ✅ **FULLY FUNCTIONAL**
- IMCA header with current date and Hijri date
- Current prayer highlighting (smart time-based logic)
- All prayer times with Athan and Iqamah from API
- Centered content layout
- Real-time data from backend
- Automatic prayer time detection

### Qibla Direction Screen ✅ **FULLY FUNCTIONAL**
- Real compass integration with device sensors
- GPS location detection
- Accurate Qibla direction calculation
- Distance to Kaaba calculation
- Real-time accuracy indicators
- Permission handling for location access

### Settings Screen ✅ **FULLY FUNCTIONAL**
- Working prayer notification toggles
- Real notification scheduling with AlarmManager
- Location permission management
- Theme preference storage
- Notification time customization
- IMCA contact information
- Settings persistence

## 🔧 Configuration

### Backend Configuration ✅ **FULLY IMPLEMENTED**
- **Location**: Indianapolis, IN (39.7684, -86.1581)
- **Time Zone**: Eastern Time
- **Prayer Calculation**: ISNA method with Hanafi madhab
- **Iqamah Delays**: IMCA-specific timing (20 min, Maghrib 5 min)

### Android Configuration ✅ **FULLY IMPLEMENTED**
- **API Base URL**: `https://prayer-app-backend-vozu.onrender.com/api/prayer-times/`
- **Network Timeout**: 30 seconds
- **Theme**: Dark mode
- **Language**: English
- **Notifications**: Configurable timing (5-30 minutes)
- **Location**: GPS and compass integration

## 🚀 Deployment

### Backend Deployment ✅ **LIVE**
The backend is automatically deployed on Render:
- **URL**: https://prayer-app-backend-vozu.onrender.com
- **Health Check**: `/api/prayer-times/today`
- **Auto-deploy**: On push to main branch

### Android Deployment ✅ **READY**
- Build APK using Android Studio or Gradle
- Test on physical device
- Distribute APK to users via QR code

## 🎯 Current Status

### ✅ **Fully Functional Features**
- Backend API with prayer time calculations
- Android app prayer times screen with real-time data
- Smart current prayer detection based on device time
- Real Qibla direction with compass and GPS
- Working settings with notification management
- Prayer time notifications with AlarmManager
- Location services integration
- Hijri date calculation and display
- Modern UI with centered content
- Complete permission handling

### 🔧 **Technical Implementation**
- StateFlow for reactive state management
- Coroutines for asynchronous operations
- ViewModel architecture
- SharedPreferences for data persistence
- BroadcastReceivers for notifications
- Sensor integration for compass
- Location services for GPS

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

*This is a complete, production-ready prayer times application with all features fully implemented and functional.* 