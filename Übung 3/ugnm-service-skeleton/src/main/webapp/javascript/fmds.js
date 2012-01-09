/**
 * @class
 * 
 * FmdClient is a JavaScript client library wrapping the access to the RESTful Web Services for Fake Media Detection created within the lab course
 * Unternehmensgruendung und Neue Medien" in WS 2011/12 at Chair for Computer Science 5 (Databases & Information Systems) at RWTH Aachen University, Germany.
 * 
 * @author <a href="mailto:renzel@dbis.rwth-aachen.de">Dominik Renzel</a>
 * 
 * @returns {FmdClient}
 */




/** ------------------------ Client ------------------- */

function FmdClient(endpointUrl){
	
	// store a couple of central resource URIs for later usage
	this._serviceEndpoint = endpointUrl;
	this._usersResource = this._serviceEndpoint + "users";
	this._mediaResource = this._serviceEndpoint + "media";
	this._achievementsResource = this._serviceEndpoint + "achievements";
	//this._collectsResource = this._serviceEndpoint + "collects";
	//this._ratesResource = this._serviceEndpoint + "rates";
	
	// since RESTful Web Services are stateless by design, credentials will have to be sent
	// with every HTTP request requiring authentication. However, users should only provide
	// credentials once and then play the game. For that purpose we use a simple mechanism
	// that stores credentials in the browser's local storage using a new HTML5 feature. 
	// Works with latest versions of Chrome and Firefox. On initialization of an FmdClient
	// instance, we check, if credentials are stored in local storage and - if so - set two 
	// client fields _uid and _cred, which can be used for authentication in subsequent requests.
	
	if(localStorage.getItem("fmdsusername") !== null && localStorage.getItem("fmdsuname") !== null && localStorage.getItem("fmdsuid") !== null && localStorage.getItem("fmdscred") !== null){
		this._username = localStorage.getItem("fmdsusername");
		this._uname = localStorage.getItem("fmdsuname");
		this._uid = localStorage.getItem("fmdsuid");
		this._cred = localStorage.getItem("fmdscred");;
	}
	
};

/**
 * Determines asynchronously, if a user with a given login is registered already.
 * The result parameter in the callback function exhibits a value true, if a user 
 * with the given login is already registered, false else.
 * 
 * @param (String) login
 * @param (function(result)) callback
 */
FmdClient.prototype.isRegistered = function(email, callback){
	$.ajax({
		url: this._usersResource + "/" + email,
		type: "GET",
		success: function(da,s){
			callback(true);
		},
		statusCode: {
			404: function(){
				callback(false);
			}
		},
		cache: false
	});
};

/**
 * Determines if a user is currently logged in and returns a boolean value of 
 * true if a user is logged in, false else.
 * 
 * @returns (boolean) 
 */
FmdClient.prototype.isLoggedIn = function(){
	if(typeof this._uid !== 'undefined' && typeof this._cred !== 'undefined'){
		return true;
	} else {
		return false;
	}
};


/**
 * Authenticates a user with the given credentials against the UGNM Web service. 
 * The callback parameter result is a boolean returning true, if authentication succeeded, false else.
 * A side effect of a successful authentication is that credentials are stored in local storage for
 * all later method calls.
 * 
 * @param login (String)
 * @param password (String)
 * @param callback (function(result)) 
 */
FmdClient.prototype.login = function(email, password, callback){
	
	// for this login step we use one HTTP operation on one resource, which is authentication-aware.
	// In this example, we use the GET operation on the resource /users/{login} including credentials
	// as HTTP auth header.
	var resource = this._usersResource + "/" + email;
	var that = this;
	
	// use helper function to create credentials as base64 encoding for HTTP auth header
	var credentials = __make_base_auth(email, password);
	
	// here, you see a first example of an AJAX request done with the help of jQuery.
	$.ajax({
		url: resource, // specify a url to which the HTTP request is sent
		type: "GET", // specify the HTTP operation
		
		// set HTTP auth header before sending request
		beforeSend: function(xhr){
			xhr.setRequestHeader("Authorization", credentials);
		},
		
		// this is one of the callbacks to be triggered on successful processing 
		// of the request by the Web service, in this case a succeeded GET including
		// successful authentication.
		dataType: 'text',
		success: function(data){
				var object = $.parseJSON(data); 
        		var name = object.name; 
        		var username = object.username;
        		var ep = object.ep;

			// set user id and credentials as fields of the client.
        	that._uname = name;
        	that._username = username;
			that._uid = email;
			that._cred = credentials;
			that._uep = ep;
			
			// store credentials in local storage
			// Local Storage version
			
			localStorage.setItem("fmdsuep",ep);
			localStorage.setItem("fmdsuname",name);
			localStorage.setItem("fmdsusername",username);
			localStorage.setItem("fmdsuid",email);
			localStorage.setItem("fmdscred",credentials);
			
			
			// Cookies version
			
			/*
			$.cookie("fmdsuid",email);
			$.cookie("fmdscred",credentials);
			*/
			
			//
			callback({status:"ok"});
		},
		
		// this is another mechanism of reacting to any answer coming from the Web service.
		statusCode: {
			// if user does not exist, return not_found failed.
			404: function(){
				callback({status:"notfound"});
			},
			// if credentials were not correct, return authentication failed.
			401: function(){
				callback({status:"unauthorized"});
			} 
		}
	});
};

/**
 * Logs out currently logged in user. Effectively, credentials in local storage and available as fields
 * _uid and _cred will be reset.
 */
FmdClient.prototype.logout = function(){
	
	// remove credentials from local storage
	localStorage.removeItem("fmdsuname");
	localStorage.removeItem("fmdsusername");
	localStorage.removeItem("fmdsuid");
	localStorage.removeItem("fmdscred");
	
	// reset client fields
	delete this._uname;	
	delete this._username;	
	delete this._uid;
	delete this._cred;
};

/**
 * Signs up a new user with given login, name and password against the UGNM Web service asynchronously.  
 * The callback parameter result is a JSON object of the form
 * 
 * 		result = {status: <STATUS>(, uri: <URI>)};
 * 
 * In the case of a successful signup, <STATUS> will contain the value "created" and <URI> the URI to 
 * the newly created resource. In the case of a failed signup, <STATUS> will contain a message about
 * the error occurred, and <URI> remains unset.
 * 
 * @param email (String)
 * @param username (String)
 * @param name (String)
 * @param password (String)
 * @param callback (function(result)) 
 */
FmdClient.prototype.signup = function(email, name, username, password, callback){
	
	// create JSON representation to be passed to the Web Service
	var d = {};
	d.email = email;
	d.name = name;
	d.username = username;
	d.password = password;
	
	var resource = this._usersResource;
	
	// do AJAX call to Web Service using jQuery.ajax
	$.ajax({
		url: resource,
		type: "PUT",
		data: JSON.stringify(d), // JSON data must be transformed to a String representation
		
		// process result in case of success and feed result to callback function passed by developer
        success: function(uri){
			var result = {};
			result.status = "created";
			result.uri = uri;
			
			callback(result);
		},
		// process result in case of different HTTP statuses and feed result to callback function passed by developer
		statusCode: {
			406: function(){
				callback({status:"notacceptable"});
			},
			500: function(){
				callback({status:"servererror"});
			},
            409: function(){
                callback({status:"conflict"});
            },

		},
		contentType: "application/json",
		cache: false
	});
};


/**
 * Updates User Profile by putting username and password
 * (real) name can not be changed
 */

FmdClient.prototype.updateUser = function(username, password, passwordNew, callback){
    
    // create JSON representation to be passed to the Web Service
        var d = {};
        d.username = username;
        d.password = passwordNew;
    
    var that = this;
    var resource = this._usersResource + "/" + this._uid;
    
    var credentials = __make_base_auth(this._uid, password);
    
    

    // do AJAX call to Web Service using jQuery.ajax
    $.ajax({
            url: resource,
            type: "PUT",
            data: JSON.stringify(d), // JSON data must be transformed to a String representation
            
            beforeSend: function(xhr){
                    xhr.setRequestHeader("Authorization", credentials);
            },
            // process result in case of success and feed result to callback function passed by developer
            success: function(){
                        //updatet authorizierung mit neuem password
            			that._cred = __make_base_auth(this._uid, passwordNew);
            			localStorage.setItem("fmdscred",that._cred);
            			that._username = username;
            			localStorage.setItem("fmdsusername",that._username);
                        
            			callback({status:"ok"});
                        
                },
            // process result in case of different HTTP statuses and feed result to callback function passed by developer
            statusCode: {
                    406: function(){
                            callback({status:"notacceptable"});
                    },
                    401: function(){
                            callback({status:"unauthorized"});
                    },
                    
            },
            contentType: "application/json",
            cache: false
    });
};


/** Deletes users profile
 *	this deletes user from database
 */
FmdClient.prototype.deleteUser = function(password, callback){
    
    var that = this;
    var resource = this._usersResource + "/" + this._uid;
    
    var credentials = __make_base_auth(this._uid, password);
    
    

    // do AJAX call to Web Service using jQuery.ajax
    $.ajax({
    		url: resource,
            type: "DELETE",
            
            beforeSend: function(xhr){
                    xhr.setRequestHeader("Authorization", credentials);
            },
            // process result in case of success and feed result to callback function passed by developer
            success: function(){
                        
            			callback({status:"ok"});
                        
                },
            // process result in case of different HTTP statuses and feed result to callback function passed by developer
            statusCode: {
                    401: function(){
                            callback({status:"unauthorized"});
                    },
                    
            },
            contentType: "application/json",
            cache: false
    });
};


/**
 * Rates a specified Medium by a logged in User.
 * The callback parameter result is a JSON object.
 */

FmdClient.prototype.rateMedium = function(m, r, callback){
	
	if(!this.isLoggedIn){
		alert("Not logged in");
	}
	var picid = m.id;
    var d = {};
    d.id = picid;
    d.rate = r;
	
    var resource = this._usersResource + "/" + this._uid + "/rates";
	
	var that = this;
	
	// do AJAX call to Web Service using jQuery.ajax
	$.ajax({
		url: resource,
		type: "PUT",
		data: JSON.stringify(d), // JSON data must be transformed to a String representation
		beforeSend: function(xhr){
			xhr.setRequestHeader("Authorization", that._cred);
		},
		// process result in case of success and feed result to callback function passed by developer
		success: function(uri){
			var result = {};
			result.status = "created";
			result.uri = uri;
			
			callback(result);
		},
		// process result in case of different HTTP statuses and feed result to callback function passed by developer
		statusCode: {
			400: function(){
				callback({status:"badrequest"});
			},
			401: function(){
				callback({status:"unauthorized"});
			},
			404: function(){
				callback({status:"notfound"});
			},
			409: function(){
				callback({status:"conflict"});
			},
			500: function(){
				callback({status:"servererror"});
			},
			
		},
		contentType: "application/json",
		cache: false
	});
}





/** ------------------------ Single-Getter-Functions ------------------- */

/**
 * Retrieves a user asynchronously. The result parameter of the callback function 
 * contains the JSON object of the form: (User-Example)
 
 * <ul>
 * 	<li>email - Email</li>
 * 	<li>username - User-Name</li>
 * 	<li>name - Full Name</li>
 * 	<li>ep - Experience Points</li>
 * </ul>
 *  ]
 * @param callback (function(object)) 
 * 
 * 
 */
FmdClient.prototype.getUserEmail = function(email, callback){
    
    var resource = this._usersResource + "/" + email;
 $.ajax({
        url: resource,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

FmdClient.prototype.getUserUri = function(uri, callback){
$.ajax({
        url: uri,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

/**
 * Retrieves a medium asynchronously. The result parameter of the callback function 
 * contains the JSON object of the form: (Medium-Example)
 
 * <ul>
 * 	<li>id - ID</li>
 * 	<li>url - URL</li>
 * 	<li>value - 1=True, 0=False</li>
 * 	<li>description - Short description</li>
 * </ul>
 *  ]
 * @param callback (function(object)) 
 * 
 * 
 */
FmdClient.prototype.getMediumId = function(mediumId, callback){
    var resource = this._mediaResource + "/" + mediumId;
$.ajax({
        url: resource,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

FmdClient.prototype.getMediumUri = function(uri, callback){
	$.ajax({
        url: uri,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

/**
 * Retrieves a achievement asynchronously. The result parameter of the callback function 
 * contains the JSON object of the form: (Achievement-Example)
 
 * <ul>
 * 	<li>id - ID</li>
 * 	<li>description - Short description</li>
 * 	<li>name - Name</li>
 * 	<li>url - URL</li>
 * </ul>
 *  ]
 * @param callback (function(object)) 
 * 
 * 
 */
FmdClient.prototype.getAchievementId = function(id, callback){
    var resource = this._achievementsResource + "/" + id;
$.ajax({
        url: resource,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var achieve = $.parseJSON(data);
              
                callback(achieve);
        },
        
});

};

FmdClient.prototype.getAchievementUri = function(uri, callback){
	$.ajax({
        url: uri,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};


/**
 * Retrieves a collect asynchronously. The result parameter of the callback function 
 * contains the JSON object of the form: (Collect-Example)
 
 * <ul>
 * 	<li>achievementId - ID</li>
 * </ul>
 *  ]
 * @param callback (function(object)) 
 * 
 * 
 */
FmdClient.prototype.getCollectId = function(collectId, email, callback){
    var resource = this._usersResource + "/" + email + "/collect/" + collectId;
$.ajax({
        url: resource,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

FmdClient.prototype.getCollectUri = function(uri, callback){
$.ajax({
        url: uri,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

/**
 * Retrieves a rate asynchronously. The result parameter of the callback function 
 * contains the JSON object of the form: (Collect-Example)
 
 * <ul>
 * 	<li>achievementId - ID</li>
 * </ul>
 *  ]
 * @param callback (function(object)) 
 * 
 * 
 */
FmdClient.prototype.getRateId = function(rateId, callback){
    var resource = this._usersResource + "/" + this._uid + "/rates/" + rateId;
    $.ajax({
        url: resource,
        type: "GET",
		dataType: 'text',
        success: function(data){
        	var object = $.parseJSON(data);
              
                callback(object);
        },
        
});

};

FmdClient.prototype.getRateUri = function(uri, callback){
	$.ajax({
	        url: uri,
	        type: "GET",
			dataType: 'text',
	        success: function(data){
	        	var object = $.parseJSON(data);
	              
	                callback(object);
	        },
	        
	});

	};


/** ------------------------ Multi-Getter-Functions ------------------- */



/**
 * Retrieves all users/media/achievements/collects/rates asynchronously. The result parameter of the callback function 
 * contains the list of all retrieved users as an array of JSON objects of the form: (Users-Example)
 * 
 *  	[<USER1>,...,<USERn>]
 *  
 * where each <USERx> contains the URI ... 
 * 
 * @param callback (function(users)) 
 * 
 * 
 */

FmdClient.prototype.getUsers = function(callback){
	
	var resource = this._usersResource;
    $.ajax({
            url: resource,
            type: "GET",
    		dataType: 'text',
            success: function(data){
            	var objects = $.parseJSON(data);
            	var users = objects.users;
                    
                    callback(users);
            },
            
    });
    
};


/**
 * Retrieves all media asynchronously. The result parameter of the callback function 
 * contains the list of all retrieved media as an array of JSON objects of the form
 * 
 *  	[<MEDIUM1>,...,<MEDIUMn>]
 *  
 * where each <MEDIUMx> contains the URI ...
 * 
 * @param callback (function(media)) 
 */

FmdClient.prototype.getMedia = function(callback){
    var resource = this._mediaResource;
   
    $.ajax({
            url: resource,
            type: "GET",
    		dataType: 'text',
            success: function(data){
            	var objects = $.parseJSON(data);
            	var media = objects.media;
                    
                    callback(media);
            },
            
    });
    
};

/**
 * Retrieves all achievements asynchronously. The result parameter of the callback function 
 * contains the list of all retrieved users as an array of JSON objects of the form: (Users-Example)
 * 
 *  	[<ACHIEVEMENT1>,...,<ACHIEVEMENTn>]
 *  
 * where each <ACHIEVEMENTx> contains the URI ... 
 * 
 * @param callback (function(achievements)) 
 * 
 * 
 */

FmdClient.prototype.getAchievements = function(callback){
	
	var resource = this._achievementsResource;
    $.ajax({
            url: resource,
            type: "GET",
    		dataType: 'text',
            success: function(data){
            	var objects = $.parseJSON(data);
            	var achievements = objects.achievements;
                    
                    callback(achievements);
            },
            
    });
    
};

/**
 * Retrieves all collects asynchronously. The result parameter of the callback function 
 * contains the list of all retrieved users as an array of JSON objects of the form: (Users-Example)
 * 
 *  	[<COLLECT>,...,<COLLECTn>]
 *  
 * where each <COLLECTx> contains the URI ... 
 * 
 * @param callback (function(collects)) 
 * 
 * 
 */

FmdClient.prototype.getCollects = function(callback){
	var resource = this._usersResource + "/" + this._uid + "/collect";
    $.ajax({
            url: resource,
            type: "GET",
    		dataType: 'text',
            success: function(data){
            	var objects = $.parseJSON(data);
            	var collects = objects.collects;
                    
                    callback(collects);
            },
            
    });
    
};


/**
 * Retrieves all rates asynchronously. The result parameter of the callback function 
 * contains the list of all retrieved users as an array of JSON objects of the form: (Users-Example)
 * 
 *  	[<RATE>,...,<RATEn>]
 *  
 * where each <RATEx> contains the URI ... 
 * 
 * @param callback (function(collects)) 
 * 
 * 
 */

FmdClient.prototype.getCollects = function(email, callback){
	
	var resource = this._usersResource + "/" + email + "/rates";
    $.ajax({
            url: resource,
            type: "GET",
    		dataType: 'text',
            success: function(data){
            	var objects = $.parseJSON(data);
            	var rates = objects.rates;
                    
                    callback(rates);
            },
            
    });
    
};

// Private helper function to create Base64 encoded credentials as needed for
// HTTP basic authentication
function __make_base_auth(user, password) {
	var tok = user + ':' + password;
	var hash = $.base64.encode(tok);
	var result = "Basic " + hash;
	return result;
}