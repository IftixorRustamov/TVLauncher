# Android TV Launcher - Clean Architecture


## 🎯 Features

- ✅ Clean Architecture (Data/Domain/UI layers)
- ✅ MVVM Pattern with ViewModel
- ✅ Efficient list updates with DiffUtil
- ✅ Material Design 3 components
- ✅ Custom launcher icon
- ✅ Swipe-to-refresh functionality
- ✅ Grid layout optimized for TV (6 columns)
- ✅ Focus management for D-pad navigation
- ✅ State persistence with SharedPreferences
- ✅ Coroutines for async operations
- ✅ LiveData for reactive UI updates

## 📁 Project Structure

```
app/src/main/java/com/example/tvlauncher/
├── data/
│   ├── model/
│   │   └── AppInfo.kt                 # App data model
│   ├── repository/
│   │   └── AppRepository.kt           # Handles app data
│   └── preferences/
│       └── LauncherPreferences.kt     # SharedPreferences manager
├── ui/
│   ├── main/
│   │   ├── MainActivity.kt            # Main launcher screen
│   │   ├── AppAdapter.kt              # RecyclerView adapter
│   │   └── GridSpacingItemDecoration.kt # Grid spacing
│   └── viewmodel/
│       └── LauncherViewModel.kt       # UI logic & state
└── utils/
    ├── Constants.kt                   # App constants
    └── DeviceUtils.kt                 # Device detection utilities
```




**Solution:**

1. Added `PackageManager.MATCH_ALL` flag
2. Implemented proper filtering to exclude system launchers
3. Used coroutines for background processing
4. Increased grid columns from 3 to 6 for TV

### Key Improvements

**Clean Architecture**
    - Separation of concerns
    - Repository pattern for data access
    - ViewModel for UI logic


```kotlin
   val resolveInfoList = packageManager.queryIntentActivities(
    mainIntent,
    PackageManager.MATCH_ALL  // ← This was missing!
)
```

**Proper Grid Sizing**
    - TV: 6 columns
    - Tablet: 4 columns
    - Phone: 3 columns


## 🎥  Video



---
