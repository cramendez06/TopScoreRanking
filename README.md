# Top Score Ranking
Basic Java Spring API Sample: Top Score Ranking

API Features
This API will be used to keep scores for a certain game for a group of player. It is designed using RESTful API design that serves and receive JSON payloads.

Create Score
The client can send a payload containing:

    - player : String containing the name of player. It will identify the players. "Edo" or "edo" or "EDO" should be the same player. But "Ed0" and "Edo" should not. 
    - score : Integer strictly superior to 0 that represents the score.
    - time : String representing date and time. 

Get Score
    - Using a simple id you can retrieve a score.

Delete Score
    - Using a simple id you can delete a score.

Get list of scores:

    - The client can request a list of scores.
    - The list can be filtered by date before or after given times. It can also be filtered by player names.
    - The list supports pagination.

Possible query on this endpoint include:

    - "Get all scores by playerX"
    - "Get all score after 1st November 2020"
    - "Get all scores by player1, player2 and player3 before 1st december 2020"
    - "Get all scores after 1 Jan 2020 and before 1 Jan 2021"
    - "Get players' history"

The client can request a player history. The resulting payload contains:

    - top score (time and score) which the best ever score of the player.
    - low score (time and score) worst score of the player.
    - average score value for player
    - list of all the scores (time and score) of this player.

To build the API and run the Unit test, you can do the following:

1) Open Command Prompt (make sure to include gradle directory into your PATH Environment Variable)
2) Go to project directory (e.g. C:\Users\crame\eclipse-workspace\Top-Score-Ranking) 
3) Type "gradlew clean build"

To run the API, you can do the following:

1) Open Command Prompt (make sure to include gradle directory into your PATH Environment Variable)
2) Go to project directory (e.g. C:\Users\crame\eclipse-workspace\Top-Score-Ranking) 
3) Type "gradlew clean bootRun"

To test the API functionalities, you can do the following in succession:

1) Register record
    - Using another Command Prompt instance, type the following CURL commands:
    
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"PLAYER1\", \"score\": \"100\", \"time\": \"20201010120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"player1\", \"score\": \"200\", \"time\": \"20201011120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"PlayeR1\", \"score\": \"300\", \"time\": \"20201012120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"PLaYer1\", \"score\": \"400\", \"time\": \"20201013120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"playeR1\", \"score\": \"500\", \"time\": \"20201014120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"PLAYER 1\", \"score\": \"600\", \"time\": \"20201015120000\"}"
          curl -X POST "localhost:8080/ranking/register" -H "Content-type:application/json" -d "{\"player\":\"PlayeR2\", \"score\": \"700\", \"time\": \"20201016120000\"}"
    
    ** Time should be entered in yyyyMMddHHmmss format 
     
2) Get all records

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -v "localhost:8080/ranking/all"
          
3) Search records by ID

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -v "localhost:8080/ranking/searchscore?id=1"
          
    ** IDs are incremented values starting from 1
    
4) Search score list by Player

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1"
          
    ** You can also do pagination by using "page" and "size" parameters
    
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1&page=0&size=3" & :: (Page 1 by 3 rows)
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1&page=1&size=3" & :: (Page 2 by 3 rows)

5) Search score list by Player with date and time filter

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1"
          
    ** You can also filter by time and date using "onbefore" and "onafter" parameters
    
          curl -v "localhost:8080/ranking/timefilter/searchlist?player=PLAYER1&onafter=20201011120000" & :: (ON and AFTER 2020/10/11 12:00:00)
          curl -v "localhost:8080/ranking/timefilter/searchlist?player=PLAYER1&onbefore=20201012120000" & :: (ON and BEFORE 2020/10/12 12:00:00)
          curl -v "localhost:8080/ranking/timefilter/searchlist?player=PLAYER1&onafter=20201011120000&onbefore=20201013120000" & :: (ON and BETWEEN 2020/10/11 12:00:00 and 2020/10/13 12:00:00)
          curl -v "localhost:8080/ranking/timefilter/searchlist?player=PLAYER1&onafter=20201012120000&onbefore=20201012120000" & :: (ON 2020/10/11 12:00:00)
          
    ** You can also do pagination by using "page" and "size" parameters
    
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1&onafter=20201010120000&page=0&size=3" & :: (Page 1 by 3 rows)
          curl -v "localhost:8080/ranking/searchlist?player=PLAYER1&onafter=20201010120000&page=1&size=3" & :: (Page 2 by 3 rows)
          
6) Search score history by player

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -v "localhost:8080/ranking/history?player=PLAYER1"
          curl -v "localhost:8080/ranking/history?player=pLayer2"
          
7) Delete record by ID

    - Using another Command Prompt instance, type the following CURL command:
    
          curl -X DELETE "localhost:8080/ranking/delete?id=6"
          
          

          
          


