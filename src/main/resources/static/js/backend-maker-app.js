angular
		.module('backend-maker-app', [ 'ngRoute', 'auth', 'navigation', 'models'])
		.config(

				function($routeProvider, $httpProvider, $locationProvider) {

					$locationProvider.html5Mode(true);

					$routeProvider.when('/', {
						templateUrl : 'js/models/models.html',
						controller : 'models',
						controllerAs : 'controller'
					}).when('/models/add', {
						templateUrl : 'js/models/addModels.html',
						controller : 'models',
						controllerAs : 'controller'
					}).when('/login', {
						templateUrl : 'js/navigation/login.html',
						controller : 'navigation',
						controllerAs : 'controller'
					}).otherwise('/');

					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

				}).run(function(auth) {

			// Initialize auth module with the home page and login/logout path
			// respectively
			auth.init('/', '/login', '/logout');

		});
