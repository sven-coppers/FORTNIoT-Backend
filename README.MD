# HassIO: middleware
## Authentication
Authenticating on the HassIO API can be done in two manners:
- Legacy API password
- Bearer Token
As Legacy API password will be deprecated soon, we recommend to use the bearer token method. Enabling bearer token is done as follows:
1. Go to your HassIO profile page
2. Scroll down and create a new long-live token
3. Create a token.json file in the resources folder of the project
4. Add a property containing your bearer token
For formatting look at the example.json