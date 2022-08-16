# Parent Android App

### About

The App will be used by end user(parents).This App will let parents see marks of their kids.

### Authentication

To use this app, users i.e parent will have to enter their phone no and password to login. Once logged in they will stay logged in unless they log out mannually. If any parent is using this app for the first time their password will be NULL in database as password is not asked while asking parent details in TeacherDesktop program.If this is the case then parent will be first asked thier phone no to sign in then they will be asked to provide date of birth of their child(in case if a parent has 2 or more child he/she can enter date of birth of any of their children) and once they enter correct date of birth of their child they will be prompted to create/enter a password that will be used for future login.

### Additional Resources Used

The Additional Resoruces that are required by the program are stored in Jar Files  directory.
* [JSON Parser](./app/libs/JSON%20Parser.jar) files help to create,parse JSON Strings.Package name has been changed from org.json to parser.json to avoid naming conflict with android's org.json package.[Download Link](https://repo1.maven.org/maven2/org/json/json/20220320/)