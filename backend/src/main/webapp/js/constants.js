var API_URL = 'https://cassini-ke.appspot.com/_ah/api';
var CLIENT_ID = '820175235736-3ph00blupd0oh8q0lhqu6g0dcohlo27v.apps.googleusercontent.com';
var SCOPES = 'https://www.googleapis.com/auth/userinfo.email';


//get inline request parameters
function get(name) {
    if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
    else
        return "null";
}