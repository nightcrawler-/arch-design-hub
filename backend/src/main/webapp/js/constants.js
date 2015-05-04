var API_URL = 'https://archdesign-hub.appspot.com/_ah/api';
var CLIENT_ID = '460866140621-arq63orifsp3i17mb4u5mnmj284meqf5.apps.googleusercontent.com';
var SCOPES = 'https://www.googleapis.com/auth/userinfo.email';


//get inline request parameters
function get(name) {
    if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
    else
        return "null";
}