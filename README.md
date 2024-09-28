# Collectible Toys Store
The «Collectible Toys» project is an online store powered by a REST API, enabling users to purchase collectible figurines.

# Technologies
 + Spring (Boot, Data, Security, Email)
 + MongoDB
 + JWT
 + Swagger
 + Firebase

## Environmental Variables
### Mail
- ```ADMIN_EMAIL:``` Administrator mail (for admin access to endpoints)
- ```MAIL_USERNAME:``` Special mail on behalf of which the program will send messages to users (for example, verification messages). Spring Email
- ```MAIL_PASSWORD:``` Password from a special mail on behalf of which the program will send messages to users (for example, verification messages). Spring Email

### Firebase
- ```BUCKET_NAME:``` Bucket name from Firebase
- ```PRIVATE_KEY_FILENAME:``` Private key that grants access to Firebase storage service

### MongoDB
- ```DB_PASSWORD:``` Database password

### Other
- ```JWT_SECRET_PROPS:``` JWT secret
- ```URI_HOST:``` uri of app hosting. To start it locally, write ```http://localhost:8080```
