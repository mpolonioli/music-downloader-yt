FROM PROJECT HOME
mvn clean compile assembly:single
java -jar .\target\music-downloader-yt-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
	-o "output_folder"	(default "is ./")
	-p "file.playlist"	(required)
	-c "true|false"		(default is "true")
	-y "youtube-dl-path"	(default is "youtube-dl")