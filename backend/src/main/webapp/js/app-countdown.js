(function () {
    var app = angular.module('countdown', []);
    var apisToLoad = 1;
    var loginAttempts = 2;

    app.config(function ($sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist([
   // Allow same origin resource loads.
   'self',
   // Allow loading from the https protocol.
   'https://kiki-ke.appspot.com/**', 'http://kiki-ke.appspot.com/**']);
    })

    app.controller('MainController', function ($scope, $window) {

        $scope.insertEmail = function (req) {
            console.log(req);
            $scope.loading = true;
            
            gapi.client.waitingUserApi.insert(req).execute(function (resp) {
                if (!resp.code) {
                    //went well
                    $scope.success = true;
                }else
                    $scope.error = true;
                
                $scope.loading = false;
                $scope.$apply();
                console.log(resp);
            });
        }




        /**
         * Login code start
         * **********************************************************************
         */
        $scope.signIn = function (mode, authorizeCallback) {
            gapi.auth.authorize({
                client_id: CLIENT_ID,
                scope: SCOPES,
                immediate: mode
            }, authorizeCallback);
        }

        $scope.userAuthed = function () {
            var request = gapi.client.oauth2.userinfo.get().execute(function (resp) {
                console.log(resp);

                if (!resp.code) {
                    // User is signed in, call my Endpoint, perfom UI set up for logged in user


                } else {
                    if (--loginAttempts != 0)
                        $scope.signIn(true, $scope.userAuthed);
                    else {
                        //prompt login
                    }
                }
            });
        }
        $scope.loadCallback = function () {
            if (--apisToLoad == 0) {
                //done cal watever from here
            }
        }

        $window.init = function () {
            gapi.client.load('waitingUserApi', 'v1', $scope.loadCallback, API_URL);
            //gapi.client.load('oauth2', 'v2', $scope.loadCallback);

        }

        /**
         * Login code end
         * **********************************************************************
         */

    });



})();