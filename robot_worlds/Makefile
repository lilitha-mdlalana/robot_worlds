# Makefile for Robot World project

.PHONY: compile test package release clean

# Variables
MVN=mvn
POM_FILE=pom.xml

# Target for compiling the code
compile:
	@echo "Compiling the application..."
	$(MVN) compile

# Target for running tests
test:
	@echo "Running tests..."
	$(MVN) test

# Target for packaging the application
package:
	@echo "Packaging the application..."
	$(MVN) -X package

# Target for a release build
release: remove-snapshot package
	@echo "Tagging the release..."
	@git tag -a "release-$(shell grep '<version>' $(POM_FILE) | sed 's/.*<version>\(.*\)<\/version>.*/\1/')" -m "Release version $(shell grep '<version>' $(POM_FILE) | sed 's/.*<version>\(.*\)<\/version>.*/\1/')"

# Remove the SNAPSHOT suffix from the version
remove-snapshot:
	@echo "Removing SNAPSHOT from version..."
	@sed -i 's/-SNAPSHOT//g' $(POM_FILE)

# Clean up
clean:
	@echo "Cleaning project..."
	$(MVN) clean
