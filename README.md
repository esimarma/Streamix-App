


[Contributors][contributors-url]
|
[MIT License][license-url]



<br />

<div align="center">
    <img src="icons/App_Icon.png" alt="Logo" width="160" height="auto"> 
</div>
    <h4 align="center">Streamix</h4>   


   <p align="center">
   Streamix is a project developed in Android Studio focused on helping the user keep track of the content they watch on streaming, allowing the user to create lists, consult information about the content and search through a vast library.
   <br />
   <a href= https://github.com/esimarma/Streamix-App><strong>Our Repository</strong></a>

  </p>
</div>

## About our Project

Our project is focused on the mobile application and the API created to power it. The application is powered by two APIs, one for entering content (movies and series), and another we created to manage users and the lists they create. We created the web application that contains the information from our API. The mobile application allows users to register and log in, as well as create lists related to streaming content (series to watch, viewed and favorites).


### Functionalities

- Laravel based API 
- APP powered by 2 APIs deveeloped in Android Studio with Java
- Website to manage the API and the users information


### Built With

- [Laravel][laravel-url]
- [Java][java-url]
- [TMDB API][tmdb-url]


### Project Structure

- Create the screens in Adobe XD

- Create the Database for the API

- Create the structure of the Main Activity to use fragment navigation
  - Set up BottomNavigationView for navigation
  - Add FrameLayout for fragment transactions
  - Default navigation set to HomeFragment

- Create the structure of the Home Fragment 
  - Fetch and display featured movies/series
  - Implement RecyclerView with ItemAdapter
  - Add search button (navigates to SearchFragment)

- Create the structure of the Search Fragment
  - Include SearchView for user input.
  - Implement RecyclerView with SearchAdapter for results.
  - Fetch data dynamically from API.

- Create the structure of the Lists Fragment
  - Add WatchlistButton, FavoriteButton, WatchedButton
  - Switch between lists dynamically
  - Fetch list data from ListMediaRepository
  - Implement RecyclerView with ItemAdapter

- Create the structure of the Profile Fragment
  - Display user information
  - Add Edit Profile button
    -  Implement ProfileEditFragment

- Create the structure of the Login Fragment
  - Email & password input fields
  - Navigation to RegisterFragment
  - Login button calls AuthRepository.loginUser()

- Create the structure of the Register Fragment
  - Email, username & password input fields
  - Register button calls AuthRepository.registerUser()

- Create the structure of the Settings Fragment
  -  Display:
    - Account settings
    - Language selection
    - GitHub repository link
    - Logout button
  - Implement SettingsAdapter for settings options list

- Create the structure of the Filter Fragment
  - Display a BottomSheetDialogFragment
  - Add a category spinner for filtering

- Create API interfaces
  - AuthApi, Handles login, register, and authentication
  - UserApi, Fetches and updates user info
  - MediaApi, Retrieves movie/series details
  - ListMediaApi, Fetches and updates user lists.

- Implement Retrofit API calls in repositories:
  - AuthRepository, Handles authentication API calls.
  - UserRepository, Fetches user details
  - MediaRepository, Retrieves media content
  - ListMediaRepository → Manages user-created lists

-  Ensure smooth navigation between fragments.

- Create the Laravel API and its endpoints
  - Connect the app to the Laravel API using Retrofit.



<!-- Screens Prototype  -->
### Prototype drawings of the Screens
The following image shows the drawings of the screens, made in Adobe XD. These were an early version of the screens.
<div align="center">
    <img src="prototype/Ecrãs/Ecrãs 2 fundo branco.png" alt="Logo" width="700" height="auto"> 
</div>



<!-- Database Structure -->
### Database Structure for the API

The following image shows the schema of the database on which our API is based. The database was designed by us using MySQL.
<div align="center">
    <img src="prototype/database_schema.png" alt="Logo" width="700" height="auto"> 
</div>



<!-- API Documentation -->
### API Endpoints

### Streamix API

#### **Authentication**

#### Login


```
   POST api/login
```


Description                                   |
:------------------------------------------ |
Logs in a user. |


#### Logout


```
   POST /api/logout
```

Description                                   |
:------------------------------------------ |
Logs out the authenticated user. |


 #### Register


```
   POST /api/register
```

Description                                   |
:------------------------------------------ |
Registers a new user. |


#### Get Logged-in User


```
   GET /api/user
```

Description                                   |
:------------------------------------------ |
Fetches the authenticated user’s details. |


#### **User Management**


#### Get User by ID


```
   GET /api/user/{id}
```

Description                                   |
:------------------------------------------ |
Fetches details of a specific user. |



#### Update User


```
   PUT /api/user/{id}

```


Description                                   |
:------------------------------------------ |
Updates user details. |


#### Delete User


```
   DELETE /api/user/{id}

```


Description                                   |
:------------------------------------------ |
Description: Deletes a user account. |



#### **User Lists**

#### Get All User Lists


```
   GET /api/userList

```


Description                                   |
:------------------------------------------ |
Retrieves all media lists created by the user. |



#### Create a New List


```
   POST /api/userList

```


Description                                   |
:------------------------------------------ |
 Creates a new watchlist or favorites list. |



 #### Get Specific List


```
   GET /api/userList/{id}

```


Description                                   |
:------------------------------------------ |
 Retrieves details of a specific user list. |



 #### Get Lists by Type


```
   GET /api/user-lists/type/{listType}/{userId}

```


Description                                   |
:------------------------------------------ |
 Retrieves user lists filtered by type |



#### Update a List


```
   PUT /api/userList/{id}

```


Description                                   |
:------------------------------------------ |
 Modifies an existing user-created list. |



#### Delete a List


```
   DELETE /api/userList/{id}

```


Description                                   |
:------------------------------------------ |
 Removes a user-created list. |


#### **Media in Lists**


#### Get All Media in Lists


```
   GET /api/listMedia

```


Description                                   |
:------------------------------------------ |
Retrieves all media items stored in user lists. |



#### Add Media to a List


```
   POST /api/listMedia

```


Description                                   |
:------------------------------------------ |
Adds a new media item to a user list. |



#### Get Media by ID


```
   GET /api/listMedia/{id}

```


Description                                   |
:------------------------------------------ |
Retrieves specific media details from a user list. |



#### Get Media in a User List


```
   GET /api/list-media/userList/{user_list_id}

```


Description                                   |
:------------------------------------------ |
Fetches all media items belonging to a specified user list. |



#### Update Media in List


```
   PUT /api/listMedia/{id}

```


Description                                   |
:------------------------------------------ |
Updates the media entry in a specific user list. |




#### Remove Media from List


```
   DELETE /api/listMedia/{id}

```


Description                                   |
:------------------------------------------ |
Deletes a media item from a user list. |




#### **Ratings**

#### Get All Ratings


```
   GET /api/rating

```


Description                                   |
:------------------------------------------ |
Retrieves all user-submitted media ratings. |



#### Submit a New Rating


```
   POST /api/rating

```


Description                                   |
:------------------------------------------ |
Allows users to rate a movie or TV show. |



#### Specific Rating

```
   GET /api/rating/{id}

```


Description                                   |
:------------------------------------------ |
Retrieves details of a specific rating. |



#### Update a Rating

```
   PUT /api/rating/{id}

```


Description                                   |
:------------------------------------------ |
Updates an existing rating for a media item. |



#### Delete a Rating

```
   DELETE /api/rating/{id}

```


Description                                   |
:------------------------------------------ |
Removes a rating from the system. |


### TMDB  API

####  Get Movie Details

```
   GET /api/movie/{id}

```


Description                                   |
:------------------------------------------ |
Retrieves details of a specific movie. |



####  Get Popular Movies

```
   GET /api/movie/popular

```


Description                                   |
:------------------------------------------ |
Fetches a list of currently trending movies. |



####   Get Top-Rated Movies

```
   GET /api/movie/top_rated

```


Description                                   |
:------------------------------------------ |
Retrieves a list of the highest-rated movies. |


####   Get TV Show Details

```
   GET /api/tv/{id}

```


Description                                   |
:------------------------------------------ |
 Fetches information about a specific TV show. |


####   Get Popular TV Shows

```
   GET /api/tv/popular

```


Description                                   |
:------------------------------------------ |
Retrieves a list of trending TV shows. |


####   Get Top-Rated TV Shows

```
   GET /api/tv/top_rated

```


Description                                   |
:------------------------------------------ |
Fetches a list of the highest-rated TV shows. |



####   Search for Media

```
   GET /api/search/{media_type}

```


Description                                   |
:------------------------------------------ |
Searches for movies or TV shows based on the specified media type. |









## License
Distributed under the MIT License. See `LICENSE.txt` for more information.

  
## Authors
- [@esimarma](https://github.com/esimarma)
- [@bea](https://github.com/theletterwriter)



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-url]: https://github.com/esimarma/Streamix-App/graphs/contributors
[license-url]: https://github.com/esimarma/Streamix-App/blob/main/LICENSE
[java-url]: https://www.java.com
[laravel-url]: https://laravel.com
[tmdb-url]: https://www.themoviedb.org/documentation/api