#yeoman-maven-plugin

Use this plugin to integrate the yeoman build into your maven build.
## Prerequisites
* npm, bower, grunt 
* maven 3

## Launched commands (default setup)
* `npm install`
* `bower install --no-color`
* `grunt test --no-color`
* `grunt build --no-color`

## Default layout
Yeoman application is placed into the *yo* directory at the top of the project hierarchy.

	pom.xml
	 - src
	   - main
	     - java
	     - webapp
	     - …
	   - test
	     - ..
	 - yo
	   package.json
	   component.json
	   Gruntfile.js
	   - app
	     index.html
	     ...
	   - test
	     ...
	   - dist
	     ...
## Usage
Plugin declaration :

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

Add the yeoman *dist* directory to the war file

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

Configure the clean plugin in order to delete generated directories


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
    
## Changelog

### V0.5 2015-08-19
* Add `bowerVariant` parameter to be able to use an alternative to *bower* : *bower-act* (Thanks [Thorsten Pohl ](https://github.com/tpohl))

### V0.4 2015-02-15
* Add `buildTool` parameter to be able to use an alternative to *grunt* : *gulp* the `gruntTestArgs` and `gruntInstallArgs` were renamed to `testArgs` and `buildArgs`. Previous parameters are still usable but deprecated (Thanks [Lukas Peleska](https://github.com/derLukers))


### V0.3 2015-02-03
* Add `skipBuild` parameter to skip the grunt build (`yo.build.skip` system propery) (Thanks [Pete Johanson](https://github.com/petejohanson))


### V0.2 2014-03-17
* Execute `grunt test` before `grunt build`. Tests can be skipped with `skipTests` parameter (`yo.test.skip` system property)
* Rename `gruntInstallArgs` parameter to `gruntBuildArgs`
* Logging executed commands
* Add `skip` parameter to skip the plugin execution (`yo.skip` system property) to skip the build (Thanks [Brad Sneade](https://github.com/bsneade))
* Add new parameters : 
  * `npmInstallArgs` for npm arguments(default value : `install`)
  * `bowerInstallArgs` for bower arguments (default value : `install --no-color`)
  * `gruntTestArgs` for grunt build arguments (default value : `test --no-color`)
  * `gruntInstallArgs` for grunt test arguments (default value : `build  --no-color`)

### V0.1 2013-04-25
* Initial version
    
## License
This project is licensed under [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)



	     
	     
	     


