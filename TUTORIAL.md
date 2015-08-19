#Tutorial : AngularJS and JAX-RS with Yeoman and Maven.

##Prerequisite
* Maven (check with maven --version)
* node / npm (check with node --version and npm --version)
* Yeoman (Check with yo --version)
* grunt (Check with grunt --version)
* bower (Check with bower --version)
* Angular generator for angular (Install with `npm install -g generator-angular`)

##Create the maven project using archetype

```
mvn -B archetype:generate \
	-DarchetypeCatalog=http://download.java.net/maven/2 \
	-DarchetypeGroupId=com.sun.jersey.archetypes \
	-DarchetypeArtifactId=jersey-quickstart-webapp \
	-DgroupId=com.github.trecloux \
	-DartifactId=yo-jaxrs-tutorial \
	-Dversion=0.1-SNAPSHOT
```
##Create the angular application using yeoman
```
mkdir yo
cd yo
yo angular yo-jaxrs-tutorial
Would you like to include Twitter Bootstrap? (Y/n) Y
If so, would you like to use Twitter Bootstrap for Compass (as opposed to vanilla CSS)? (Y/n) n
Would you like to include angular-resource.js? (Y/n) n
Would you like to include angular-cookies.js? (Y/n) n
Would you like to include angular-sanitize.js? (Y/n) n
..

```
##Configure grunt to proxy REST requests 

### Add the grunt-connect-proxy module
```
npm install grunt-connect-proxy --save-dev
```
### Edit Gruntfile.js to declare the proxy
```
// Just after : var lrSnippet
var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;

/* …………… */

// Modify the connect task configuration : add proxies section and insert 'proxySnippet' in the middleware
    connect: {
      // Proxy requests starting with /webresources to the server on port 8080
      proxies: [
        {
          context: '/webresources',
          host: 'localhost',
          port: 8080,
          https: false,
          changeOrigin: false
        }
      ],
      options: {
        port: 9000,
        hostname: 'localhost'
      },
      livereload: {
        options: {
          middleware: function (connect) {
            return [
              proxySnippet,
              lrSnippet,
              mountFolder(connect, '.tmp'),
              mountFolder(connect, yeomanConfig.app)
            ];
          }
        }
      },
      
/* ………… */

// Modifify the server task to ass the configureProxies step
  grunt.registerTask('server', [
    'clean:server',
    'coffee:dist',
    'compass:server',
    'configureProxies', // add this line
    'livereload-start',
    'connect:livereload',
    'open',
    'watch'
  ]);

```

## Modify maven build to launch yeoman build and include yeoman build into the war file 
```
<plugin>    
    <groupId>com.github.trecloux</groupId>
    <artifactId>yeoman-maven-plugin</artifactId>
    <version>0.5</version>
    <executions>
        <execution>
            <goals>
                <goal>build</goal>
            </goals>
        </execution>
    </executions>
</plugin>
<plugin>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.3</version>
    <configuration>
        <webResources>
            <resource>
                <directory>yo/dist</directory>
            </resource>
        </webResources>
    </configuration>
</plugin>
<plugin>
    <artifactId>maven-clean-plugin</artifactId>
    <version>2.5</version>
    <configuration>
        <filesets>
            <fileset>
                <directory>yo/dist</directory>
            </fileset>
            <fileset>
                <directory>yo/.tmp</directory>
            </fileset>
            <fileset>
                <directory>yo/app/components</directory>
            </fileset>
            <fileset>
                <directory>yo/node_modules</directory>
            </fileset>
        </filesets>
    </configuration>
</plugin>
```

## Simple call from Angular app to the JAX-RS server

### Edit yo/app/scripts/controllers/main.js to add the server call.
```
'use strict';

angular.module('yoJaxrsTutorialApp')
  .controller('MainCtrl', function ($scope, $http) {
    $http.get('./webresources/myresource').success(function(data){
      $scope.serverSays = data;
    })
  });

```

### Edit yo/app/views/main.html to display the server call
```
 <div class="hero-unit">
      <h1>Server says : {{serverSays}}</h1
 </div>

```

## Dev mode
 * Launch the server webapp using your favorite IDE, using a light server like Tomcat (Context /, on port 8080)
 * Download dependencies and then launch the grunt server

```
npm install
bower install
grunt server
```
And enjoy this awesome web application :)

## Deploy mode
Maven will build the Angular app using yeoman/grunt/bower, you can simply test your war file using classical maven command : `mvn clean install`






	