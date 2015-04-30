ZIP_NAME=e1429587
DIR_NAME=dst15

all: zip

zip:
	mvn clean
	zip -r $(ZIP_NAME).zip ../$(DIR_NAME) -x '*/.idea/*' '*.iml' '*.pdf' '*.md' '*/Makefile' '*/target/*' '*/*.zip' '*/.git/*' '*/.gitignore' 'theory1'
