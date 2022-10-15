# weatherbuddy
WeatherBuddy app is an app that displays to the user his current weather details based on his location. Also a weather forecast, that returns the forecast each 3hrs 
for many days, is displayed. Weather details are cached locally using networkBoundResource and are synced whenever the user has internet connection.\
Location is fetched
through a locationRequest callback whenever user's location is turned on. User can search for a city with a list of cities exported from https://openweathermap.org/. 
These are the actual cities supported by the weather APIS used.\
User can add a city to favorites and the mehanism is done locally through room database. Favorite cities
are stored and retrieved in a favorites screen and weather data for each city can be displayed based on its' coordinates that are retrieved from the json file containing
those cities.\
A settings screen allows users to change temeperature unit instantly and it will be reflected in the app. The unit in question is sent to the api as a querry
parameter dynamically populated from the Datastore into the RequestInterceptor.\
A notification is sent daily at 6AM to display today's weather.

The application has been written in kotlin.\
Architechture used is Clean Architecture + MVVM including usecases.\
Jetpack Libraries used: Datastore, HILT, navigation, DataBinding, ViewModel, Coroutines, Room.\
Two flavors have been created: Production and Staging for future use.\
Data is cached in the room database and are retrieved through networkBoundResource.\
HILT has been used to inject dependencies into the code.\
Notification is sent using AlarmManager instead of WorkManager because WorkManager doesn't provide the ability to set an exact time and the notification needs to be sent
at 6AM.\
Flows have been associated to coroutines to emit data to the views\
Weather and forecast data are retrieved from https://openweathermap.org/ that provides a free API_KEY\
Retrofit2 has been used for remote API fetching.\
Glide is used to load weather icons\
Dexter is used for permissions\
Single Activity architecture is used


The app code needs cleanup and the work of importing and exporting favorite cities to CSV is in progress. More time is need to write unit tests 
