# ParentAndroidAppAuthentication

### Authentication
To use this app, users i.e parent will have to enter their phone no and password to login. Once logged in they will stay logged in unless they log out mannually. If any parent is using this app for the first time their password will be NULL in database as password is not asked while asking parent details in TeacherDesktop program.If this is the case then parent will be first asked thier phone no to sign in then they will be asked to provide date of birth of their child(in case if a parent has 2 or more child he/she can enter date of birth of any of their children) and once they enter correct date of birth of their child they will be prompted to create/enter a password that will be used for future login.

### Shared Preferences

Once a user login's in a shared preference will be created with name "login_data". This shared preference will be checked while opening app to directly login in users that have previously entered correct credentials.

This Shared Preference will contain "phone" containing phone no of user who has previously logged in.

The above statements will also be executed when a new user registers.