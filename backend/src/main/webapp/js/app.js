(function () {
    var app = angular.module('app', []);
    var apisToLoad = 5;
    var loginAttempts = 2;

    app.config(function ($sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist([
   // Allow same origin resource loads.
   'self',
   // Allow loading from the https protocol.
   'https://archdesign-hub.appspot.com/**', 'https://archdesign-hub.appspot.com/**']);
    })


    app.controller('MainController', function ($scope, $window) {

        //actions
        $scope.getUploadUrl = function (url) {
            req = {};
            req.ownerId = url;
            gapi.client.uploadUrlApi.get(req).execute(function (resp) {
                if (!resp.code) {
                    $scope.uploadUrl = resp.url;
                    $scope.nav = "uploadPics";
                    $scope.$apply();
                }

            });
        }

        $scope.listAgents = function () {
            gapi.client.agentApi.list().execute(function (resp) {
                console.log(resp);
                if (!resp.code) {
                    $scope.agents = resp.items;
                }
                $scope.$apply();
            })
        }

        $scope.insertAgent = function (req) {
            $scope.loading = true;
            gapi.client.agentApi.insert(req).execute(function (resp) {
                console.log(resp);
                if (!resp.code) {
                    $scope.getUploadUrl(resp.id);
                }
                $scope.loading = false;
                $scope.$apply();

            });

        }

        $scope.insertListing = function (req) {
            $scope.loading = true;
            gapi.client.propertyApi.insert(req).execute(function (resp) {
                console.log(resp);

                if (!resp.code) {
                    $scope.getUploadUrl(resp.id);
                }
                $scope.loading = false;
                $scope.$apply();
            });
        }
        $scope.loginSuccess = function (user) {
            $scope.loggedIn = true;
            $scope.user = user;
            $scope.listAgents();
            $scope.$apply();
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
                    $scope.loginSuccess(resp);

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
                //auto sign in
                $scope.signIn(true, $scope.userAuthed());
            }
        }

        $window.init = function () {
            gapi.client.load('agentApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('propertyApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('userApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('uploadUrlApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('oauth2', 'v2', $scope.loadCallback);

        }

        /**
         * Login code end
         * **********************************************************************
         */

    });


})();