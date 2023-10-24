# Webcrawler-demo

A Java application to browse a website in search of a term supplied by the user and list the URLs where the term was found.

Request:
> POST /crawl HTTP/1.1
> Host: localhost:4567
> Content-Type: application/json
> Body: {"keyword":"something"}

Response:
> 200 OK
> Content-Type: application/json
> Body: {"id": "67hjyt56"}

GET: queries search results.
Request:
> GET /crawl/67hjyt56 HTTP/1.1
> Host: localhost:4567

Response:
> 200 OK
> Content-Type: application/json
> {
> "id": "67hjyt56",
> "status": "active",
> "urls": [
> "http://temp.com/index4.html",
> "http://temp.com/htmlman1/chelp.1.html"
> ]
> }

The base URL of the website is determined by an environment variable. Searches must follow links in subpages (if they share the same base URL).

The application supports the execution of multiple simultaneous searches. Information about searches in progress (status active) or already completed (status done) are kept indefinitely while the application is running.

While a search is in progress, its results already found are returned by the GET Operation.
