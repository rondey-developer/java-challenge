## Java-challenge

### Things that i have done.
1. Fix bug
- cannot update employee
- employee will become null after adding

2. Implement Authentication System
- JWT token
- login and register api
- Swagger with authentication

3. Logger
- replace sout to logger

4. Add test case
- Repositiory test
  - Employee
  - Login information
- Controller test
  - Employee CRUD process
  - Login and authentication process
- Intergration test
  - Stimulation on getting all employee information after login.
  
  
### Difficulties:
#### Limit time
- Problem: 
  - limited time of work
- How i tackle this problem:
  1. Planning
  2. Avoid over-programming
  - Not to implement authorities while working on authenication
  
### Planning and Execution:
- https://github.com/rondey-developer/java-challenge/projects/1
- Try to list out all then work that i should be done in 3 days
- Tag with Story, Bug and Test
- Prioritize it
- Management this project in. scrum style.

### If I have more time:
- Implement database caching
- Implement authorities while authentication
- Implement JWT token renewal mechanism
- Implement JWT validation service
- Implement mass update and mass create of employee


### Instructions
Since authentication system is introduced. Please do following step in order to authenticate
- Registration
  - make a post request to /api/v1/register with { username: $your_input, password: $your_input }
- Login
  - make a post request to /api/v1/login with { username: $your_input, password: $your_input } you just registered
  - get the token response from the server in header in order to authenticate afterward
  
- Authenticate
  - Add the token with Authorization: Bearer: $your_token in header of you request.
  - can only access all the api for employee with token
  
### After words
I ensured that both of RondeyChengTszkan and rondey-developer are the same person. Reason for two account is because there two git account in my pc. 
