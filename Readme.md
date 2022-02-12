docker build . -t tickoon/aassetmanagment-server-rest-api

docker docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 tickoon/aassetmanagment-server-rest-api