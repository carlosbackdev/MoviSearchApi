# 🎥 Spring Boot API IA for Movie Search and Chatbot Integration 🧠

Welcome to the **Spring Boot API** powering the **Movie Search Application**! This API is the backbone of the platform, enabling advanced features like **phrase-based movie searches**, **chatbot interactions**, and **user data management**. It integrates with multiple external APIs, including **OpenAI**, **Google Translate**, **TMDB**, **Firebase**, and **Datamuse**, to deliver a seamless and intelligent user experience.
Is the Backend part of project MovieSearch App Angular

All for discover movies or series by any phrase like
---
## Technologies Used 🛠️

- **Spring Boot**: Core framework for building the API. 🟢
- **Java**: languaje. 🔴
- **MySQL**: Database for storing user data and interactions. 🐬
- **JWT**: For security, spring use a tokens for authentification. 🧑‍🦱
- **OpenAI API**: For chatbot and advanced NLP tasks. 🧠
- **Google Translate API**: For multilingual support. ✍️
- **TMDB API**: For movie and series data. 🎥
- **Datamuse API**: For enhancing search capabilities. 📘
- **Firebase**: For authentication and real-time updates. 🔥

---

## Key Features 🌟

### 1. **Phrase-Based Movie Search** 🎥
- **Natural Language Processing (NLP)**: Analyzes user phrases using AI tools.
- **TMDB Integration**: Searches the TMDB database to find relevant movies based on the processed phrases.
- **Advanced Search**: Handles complex queries and returns detailed movie information (description, cast, ratings, etc.).

### 2. **Chatbot Integration with OpenAI** 🤖
- **OpenAI API**: Powers a conversational chatbot that can answer user queries, provide movie recommendations, and assist with searches.
- **Dynamic Responses**: Generates human-like responses using OpenAI's GPT models.
- **Language Translation**: Uses **Google Translate API** to support multilingual interactions.

### 3. **User Management** 👤
- **MySQL Database**: Stores user data, including:
  - Registration details
  - Saved movies and series
  - Custom lists
  - Reviews and comments
  - Search history
- **Personalized Experience**: Users can create lists, save movies, and share their collections with others.

### 4. **Machine Learning Readiness** 🧠
- **Data Collection**: All user interactions, searches, and preferences are stored for future machine learning models.
- **Analytics**: Enables insights into user behavior and preferences for personalized recommendations.

### 5. **Integration with Multiple APIs** 🔗
- **TMDB API**: Fetches movie and series data.
- **OpenAI API**: Handles chatbot interactions and complex NLP tasks.
- **Google Translate API**: Supports multilingual queries and responses.
- **Datamuse API**: Enhances search capabilities with synonym and phrase suggestions.
- **Firebase**: Manages authentication and real-time data updates.

---

## How It Works ⚙️

### 1. **User Input** ⬅️
- The user enters a phrase or query (e.g., "Find me a movie about space adventures").

### 2. **Phrase Processing** 📘
- The API uses **OpenAI** and **Google Translate** to analyze and process the phrase.
- Complex NLP tasks are handled by OpenAI, while language translation is managed by Google Translate.

### 3. **Movie Search** 🎥
- The processed query is sent to the **TMDB API** to fetch relevant movies.
- Results are filtered and returned to the user with detailed information.
 
### 4. **Chatbot Interaction** 🗃️
- The chatbot, powered by **OpenAI**, engages with the user to provide additional information, recommendations, or assistance.

### 5. **Data Storage** 🐬
- All user data, including searches, saved movies, lists, and reviews, is stored in a **MySQL database**.
- Search history and interactions are logged for future machine learning analysis.

---


### Machine Learning 🧠
- **Data Collection**: All user interactions are stored for future ML models.
- **Analytics**: Enables personalized recommendations and insights.

---

## Endpoints Overview 📡

### Movie Search
- `GET /api/movies/search?query={phrase}`: Searches for movies based on user phrases.

### Chatbot
- `POST /api/chatbot`: Interacts with the OpenAI-powered chatbot.

### User Management
- `POST /api/users/register`: Registers a new user.
- `POST /api/users/login`: Authenticates a user.
- `GET /api/users/{userId}/lists`: Retrieves user-specific lists.
- `POST /api/users/{userId}/lists`: Creates a new list for the user.

### Reviews and Comments
- `POST /api/movies/{movieId}/reviews`: Adds a review for a movie.
- `GET /api/movies/{movieId}/reviews`: Retrieves reviews for a movie.

---

---

## Get Started 🎬
For more information, please contact us at [carlosbackdev](https://carlosbackdev.com).

✨ Stay tuned for more updates! ✨


👉 [API Documentation](#)

---

**Note**: This API is designed to provide a robust and intelligent backend for movie search and chatbot applications. It combines multiple technologies to deliver a seamless and personalized user experience. 🎥🤖
