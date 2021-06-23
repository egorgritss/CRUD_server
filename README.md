Server:

Server is running on localhost:8080
Database type: Yours
Database location: Yours

Java runtime version required: 11
Start by navigating ti build/libs and executing jar file.

Descriprion: 

Server to handle CRUD operations on databese. 
+one additional operation: addPlayers. URL: /api/v1/teams/{id}/add_players
    will add provided players to team, after checks, if player is not playing for another team and team does have free slots left.
    
