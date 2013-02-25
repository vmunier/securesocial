---
file: configuration
---
# Configuration

Create a `securesocial.conf` file within your app's conf directory and include it from your `application.conf` file.  Eg:

	:::bash
	include "securesocial.conf"

## Mail settings

If you plan to use the `UsernamePasswordProvider` you need to configure your mail service. SecureSocial uses the [mailer plugin](https://github.com/typesafehub/play-plugins/tree/master/mailer) from Typesafe.

These settings go in the `smtp` section of the `securesocial.conf` file:

	:::bash
	smtp {
		host=smtp.gmail.com
		#port=25
		ssl=true
		user="your_user"
		password=your_password
		from="your_from_address"
	}

## Global Settings

- `onLoginGoTo`: SecureSocial tries to redirect the user back to the page they intended to access after login.  There are cases where this can't be done (eg: the user tried to POST a form) or accessed the login page directly.  Adding the `onLoginGoto` property allows SecureSocial to redirect the user to the page you need.  

- `onLogoutGoTo`: The page where the user is redirected to after logging out.

- `ssl`: You can enable SSL for OAuth callbacks, the login, signup and reset password actions of the `UsernamePasswordProvider` and for the cookie used to trace users (you'll want this in production mode).

- `assetsController`: This setting is optional.  It is only needed if you are not using the default Assets controller provided by Play.  The value must be the full qualified class name of your controller prepended by the word Reverse.

## Authenticator Cookie

SecureSocial uses a cookie to trace authenticated users.  A `cookie` section can be added to customize it with the following properties:

- `name`: The cookie name (defaults to 'id').

- `path`: The path for which the cookie should be sent by the browser (defaults to /)
          
- `domain`: The domain for which the cookie should be sent (it is left empty by default)

- `httpOnly`: If set to true, the cookie is not readable by a client side script (defaults to true).

- `idleTimeoutInMinutes`: The amount of time the session id will remain valid since the last request (defaults to 30).

- `absoluteTimeOutInMinutes`: The amount of time the session id will be valid since the user authenticated. After this the user will need to re-authenticate (defaults to 720 minutes - 12 hours)

- `makeTransient`: Makes the cookie transient (defaults to true). Transient cookie are recommended because the cookie dissapears when the browser is closed.  If set to false, the cookie will survive browser restarts and the user won't need to login again (as long as the idle and absolute timeouts have not been passed).

## Sample configuration

All the settings go inside a `securesocial` section as shown below:

    :::bash    
	securesocial {
		#
		# Where to redirect the user if SecureSocial can't figure that out from
		# the request that was received before authenticating the user
		#
		onLoginGoTo=/

		#
		# Where to redirect the user when he logs out. If not set SecureSocial will redirect to the login page
		#
		onLogoutGoTo=/login

		#
		# Enable SSL 
		#
		ssl=false	

		#
		# The controller class for assets. This is optional, only required
		# when you use a custom class for Assets.
		#
		assetsController=controllers.ReverseMyCustomAssetsController

		 cookie {
                #name=id
                #path=/
                #domain=some_domain
                #httpOnly=true
                #idleTimeoutInMinutes=30
                #absoluteTimeOutInMinutes=720
        }
	       
	}


## Provider settings

The configuration for each provider needs to be added within the `securesocial` section as well. 

### Username Passsword Provider

The following properties can be configured:

- `withUserNameSupport`: depending on your app you can decide to use email addresses or usernames for login.  When this setting is set to `false`, the username field will be hidden in the sign up form and the email address/password combination will be used to authenticate the user.  When set to `true` usernames will be used.

- `sendWelcomeEmail`: if the to `true` a welcome email will be sent to users after sign up.

- `enableGravatarSupport`: if set to `true` Gravatar will be used to retrieve a profile image for the user.  If set to `false` it will be left empty.

- `signupSkipLogin`: if set to `true`, the user will be automatically signed in when they complete sign up. If set to `false` the user will have to sign in after registering. Username/password only.

- `tokenDuration`: Every time a user signs up or attempts a password reset SecureSocial will generate a token that identifies that request.  Each token has an expiration date and this property is used to compute it. This value is expressed in **minutes** and is set to 60 by default.

- `tokenDeleteInterval`: This property defines how often the `deleteExpiredTokens()` method in `UserService` gets called. This value is expressed in **minutes** and is set to 5 by default.

- `minimumPasswordLength`: Defines the minimum password length the user can enter. Defaults to 6 if not specified. 

- `enableTokenJob`: Enables/disables the background job used to delete sign up and reset password tokens.

- `hasher`: Specifies the current password hasher. 

For example:

	:::bash
	userpass {		
		withUserNameSupport=false
		sendWelcomeEmail=true
		enableGravatarSupport=true
		signupSkipLogin=true
		tokenDuration=60
		tokenDeleteInterval=5
		minimumPasswordLength=8
		enableTokenJob=true
		hasher=bcrypt
	}

### OAuth based Providers	

The configuration for these providers is simple, just specify the endpoints and OAuth values generated by the external service.

A configuration would like like:

	:::bash
	twitter {
		requestTokenUrl="https://twitter.com/oauth/request_token"
		accessTokenUrl="https://twitter.com/oauth/access_token"
		authorizationUrl="https://twitter.com/oauth/authenticate"
		consumerKey=your_consumer_key
		consumerSecret=your_consumer_secret
	}

	facebook {
		authorizationUrl="https://graph.facebook.com/oauth/authorize"
		accessTokenUrl="https://graph.facebook.com/oauth/access_token"
		clientId=your_client_id
		clientSecret=your_client_secret
		# this scope is the minimum SecureSocial requires.  You can add more if required by your app.
		scope=email
	}

	google {
		authorizationUrl="https://accounts.google.com/o/oauth2/auth"
		accessTokenUrl="https://accounts.google.com/o/oauth2/token"
		clientId=your_client_id
		clientSecret=your_client_secret
		scope="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"
	}

	linkedin {
		requestTokenUrl="https://api.linkedin.com/uas/oauth/requestToken"
		accessTokenUrl="https://api.linkedin.com/uas/oauth/accessToken"
		authorizationUrl="https://api.linkedin.com/uas/oauth/authenticate"
		consumerKey=your_consumer_key
		consumerSecret=your_consumer_secret
	}
    	
	github {
		authorizationUrl="https://github.com/login/oauth/authorize"
		accessTokenUrl="https://github.com/login/oauth/access_token"
		clientId=your_client_id
		clientSecret=your_client_secret
	}

	foursquare {
    	authorizationUrl="https://foursquare.com/oauth2/authenticate"
        accessTokenUrl="https://foursquare.com/oauth2/access_token"
		clientId=your_client_id
		clientSecret=your_client_secret
	}

    xing {
     	requestTokenUrl="https://api.xing.com/v1/request_token"
		accessTokenUrl="https://api.xing.com/v1/access_token"
        authorizationUrl="https://api.xing.com/v1/authorize"
		consumerKey=your_consumer_key
		consumerSecret=your_consumer_secret
    }

    instagram {
        authorizationUrl="https://api.instagram.com/oauth/authorize"
      	accessTokenUrl="https://api.instagram.com/oauth/access_token"
		clientId=your_client_id
		clientSecret=your_client_secret
    }

	vk {
	    authorizationUrl="http://oauth.vk.com/authorize"
	    accessTokenUrl="https://oauth.vk.com/access_token"
		clientId=your_client_id
		clientSecret=your_client_secret
	}

To get the `clientId`/`clientSecret` or `consumerKey`/`consumerSecret` keys you need to log into the developer site of each service (eg: Twitter, Facebook) and register your application.

*Hint: you can use the `securesocial.conf` file in the sample apps as a starting point.*

## Clustered environments

SecureSocial uses the Play cache to store values while signing in users via OAuth.  If you have more than one server then make sure to use a distributed cache (eg: memcached).
