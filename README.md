# Http-Server

8/27/2019 - Added checks to issuer that no unauthorized files are sent to a user.

8/27/2019 - Updated responses to send files/images and moved images into their own folders.

8/27/2019 - Updated reponse codes to be correct on errors and when files are not found.

8/26/2019 - Created functions to parse and dynamically create responses. Parsed responses are now stored in a Request object.

8/26/2019 - Implemented reader for reading the full HTTP request. Plan to parse this data next.

8/26/2019 - Implemented Threads to handle the http responses. The server currently however only responds with the home webpage.

8/10/2019 - Removed comments from index.html due to a bug when passed through the http server. Commented copy created instead called index-comments.html.

8/10/2019 - Completed a simple home page for the http server to respond with. It currently contains no links.

8/9/2019 - Added Navigation bar to Home Page. Issues with Positioning.

8/9/2019 - This is a basic http server that returns an exteremly simple html page.
