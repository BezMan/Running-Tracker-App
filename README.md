# Running Tracker App
Monitor your walks and runs 🏃‍♀️🏃‍♂️ 
Save and display run times, distances and speed information.

<br/>

<div class="row">
<img src="/images/screenshots/scr1.PNG" width="300">
<img src="/images/screenshots/scr2.PNG" width="300">
<img src="/images/screenshots/scr3.PNG" width="300">
<img src="/images/screenshots/scr4.PNG" width="300">
<img src="/images/screenshots/scr5.PNG" width="300">
</div>

<br/>

### Kotlin project Containing:
- Architecture Components: MVVM, Room, Live Data, Navigation.
- Location based app, using Google Maps API and drawing paths on MapView.
- Dependency Injection using Dagger Hilt (AppModule, ServiceModule).
- Coroutine for updating and displaying formatted timer clock.
- TypeConverter for storing Bitmap images in Room Database.
- Transitioning between Fragments with Bottom Navigation View.
- Sorting and displaying list of saved runs in various ways.
- Foreground Service displaying app notification with timer and action buttons.
- SharedPreferences for saving and editing user name and weight.
- Securing map API key value with gradle properties.


<br/>

### Useful Libraries:
[Timber](https://github.com/JakeWharton/timber)

[Glide](https://github.com/bumptech/glide) 

[EasyPermissions](https://github.com/googlesamples/easypermissions) 

[Chart View](https://github.com/PhilJay/MPAndroidChart) 



<br/>

### Map API Setup:
You must create a key for your project and insert in manifest file. I am securing my map API key, you can do the same by adding to your gradle.properties file: `MAP_API_KEY="your_key"`

<br/>


### Credits:
This app was built thanks to Philipp Lackner's tutorials: https://github.com/philipplackner/RunningAppYT
check out his awesome projects.

<br/>
