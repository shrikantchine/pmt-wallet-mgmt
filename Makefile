# Default module if none is provided
MODULE ?= user-mgmt-service

# This helper finds the gradlew script inside the specific module folder
GRADLEW_PATH := ./$(MODULE)/gradlew

.PHONY: help clean-all build test run

## help: Show available commands
help:
	@echo "Usage: make [target] [MODULE=module-name]"
	@echo ""
	@echo "Targets:"
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/## //'

## build: Build a specific module. Usage: make build MODULE=module2
build:
	@cd $(MODULE) && ./gradlew build -x test

## run: Run a specific module. Usage: make run MODULE=module1
run:
	@cd $(MODULE) && ./gradlew bootBuildImage

## test: Run tests for a specific module. Usage: make test MODULE=module1
test:
	@cd $(MODULE) && ./gradlew test

## clean-all: Clean all modules manually by looping through them
clean-all:
	@for dir in module1 module2 module3; do \
		if [ -f $$dir/gradlew ]; then \
			echo "Cleaning $$dir..."; \
			cd $$dir && ./gradlew clean && cd ..; \
		fi \
	done