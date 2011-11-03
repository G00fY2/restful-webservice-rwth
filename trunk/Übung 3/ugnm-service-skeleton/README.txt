UGNM WS 2011/12 FAKE MEDIA DETECTION GAME SERVICE
=================================================

  (Customize your database connection in src/main/resources/applicationContext.xml, if not done already.)

  Start the service with

      mvn jetty:run
	
  After a successful start the service is available under the base URL 
  
	localhost:8080/ugnm-service/
	
  The following resources are available under localhost:8080/ugnm-service/resources
  
	/users - collection resource; GET (retrieve list of all users), PUT (create new user as subresource)
	/users/{login} - single resource; GET (retrieve user details), POST (update user details), DELETE (delete user)

