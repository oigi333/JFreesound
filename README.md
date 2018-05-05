**JFreesound** is a Freesound API client library for Java, written in Java.

### Build
In order to contribute to the project you'll need to clone
it from the github repository and build it and test it manually.
For testing you'll need to include a resource directory in
your builds with a `config.json` file within it. The config file
contains the API token used to access the Freesound API for testing,
that you'll need to acquire for yourself. The file follows the following
format:
```json
{
    "token": "YOUR_API_TOKEN"
}
```
