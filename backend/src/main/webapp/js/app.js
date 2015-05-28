(function () {
    var app = angular.module('app', []);
    var apisToLoad = 10;
    var loginAttempts = 2;

    app.config(function ($sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist([
   // Allow same origin resource loads.
   'self',
   // Allow loading from the https protocol.
   'https://archdesign-hub.appspot.com/**', 'https://archdesign-hub.appspot.com/**']);
    })


    app.controller('MainController', function ($scope, $window) {

        //generic functions
        $scope.getRelativeTime = function (millis) {
            var date = new Date(0);
            date.setUTCMilliseconds(millis);
            if (millis == 0 || millis == null)
                return "eons";
            return moment(date).fromNow();
        }

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

        $scope.listUsers = function () {
            gapi.client.userApi.list().execute(function (resp) {
                if (!resp.code) {
                    $scope.users = resp.items;
                }
                $scope.$apply();


            });
        }

        $scope.listComments = function () {
            gapi.client.commentApi.list().execute(function (resp) {
                if (!resp.code) {
                    $scope.comments = resp.items;
                }
                $scope.$apply();
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
            req.time = new Date().getTime();
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
            req.time = new Date().getTime();
            gapi.client.propertyApi.insert(req).execute(function (resp) {
                console.log(resp);

                if (!resp.code) {
                    $scope.getUploadUrl(resp.id);
                }
                $scope.loading = false;
                $scope.$apply();
            });
        }

        $scope.insertEvent = function (req) {
            //usedd to show relevant progress bars
            $scope.loading = true;
            var dates = req.time.split("/");
            var date = new Date(dates[0], dates[1] - 1, dates[2], 8, 0, 0);
            req.time = date.getTime();
            gapi.client.eventApi.insert(req).execute(function (resp) {
                if (!resp.code) {
                    $scope.getUploadUrl(resp.id);
                }
            });
        }

        $scope.insertOrUpdateContact = function (req) {
            $scope.loading = true;
            if (req.id) {
                gapi.client.contactApi.update(req).execute(function (resp) {
                    console.log(resp);
                    if (!resp.code) {
                        $scope.contact = resp;
                        $scope.getUploadUrl(resp.id);

                    }
                    $scope.loading = false;
                    $scope.$apply();

                });

            } else {
                gapi.client.contactApi.insert(req).execute(function (resp) {
                    console.log(resp);

                    if (!resp.code) {
                        $scope.contact = resp;
                        $scope.getUploadUrl(resp.id);

                    }
                    $scope.loading = false;
                    $scope.$apply();
                });
            }
        }

        $scope.updateComment = function (comment) {
            $scope.loading = true;
            comment.replyTime = new Date().getTime();
            comment.response = comment.responseRaw;
            gapi.client.commentApi.update(comment).execute(function (resp) {
                console.log(resp);
                if (!resp.code) {
                    $scope.reply = false;
                    comment.responseRaw = "";
                    //sends gcm message to notify clients to download the new message, 0 is hardcoded on clients to exclude actuall messages. the rest of crud actions will send notifications from the backend after upload of pics is done, seeing as all of them require it
                    $scope.sendGcmMessage(0);
                } else {
                    $scope.reply = true;
                }

                $scope.loading = false;
                $scope.$apply();

            });
        }

        $scope.uploadMessage = function (message) {
            message.time = new Date().getTime();
            $scope.loading = true;
            gapi.client.messageApi.insert(message).execute(function (resp) {
                if (!resp.code) {
                    //success
                    //sends gcm message to notify clients to download the new message
                    $scope.sendGcmMessage(1);
                    $scope.loading = false;
                    $scope.nav = 'home';
                }
                $scope.loading = false;

            });
        }

        $scope.sendGcmMessage = function (message) {
            var reqObject = {};
            reqObject.message = message;
            gapi.client.messaging.messagingEndpoint.sendMessage(reqObject).execute(function (resp) {
                if (!resp.code) {
                    //success sent
                }

            });
        }

        $scope.loginSuccess = function (user) {
            $scope.loggedIn = true;
            $scope.user = user;
            $scope.listAgents();
            $scope.listUsers();
            $scope.listComments();
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
            gapi.client.load('eventApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('contactApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('messageApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('messaging', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('commentApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('uploadUrlApi', 'v1', $scope.loadCallback, API_URL);
            gapi.client.load('oauth2', 'v2', $scope.loadCallback);

        }

        /**
         * Login code end
         * **********************************************************************
         */

    });


})();