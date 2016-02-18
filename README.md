AP Android JustAPI Gateway
===========

This is an SDK that you will use to interface with the AnyPresence's JustAPIs technology.


Dependencies
===========

These dependencies needed are:

GSON:
compile 'com.google.code.gson:gson:2.5'

Guava:
compile 'com.google.guava:guava:18.0'

OKHttp:
compile 'com.squareup.okhttp:okhttp:2.7.4'

AP's JustAPI Java core library:
APGW-[version]-SNAPSHOT.jar


Introduction
==========

The main class to work with is APAndroidGateway. You will use an instance of this class to make requests.

An instance should be created using a builder. A 'Context' is needed as well. You will want to use the application's
Application context. This is needed because it utilizes Android volley and it's needed to construct a request queue
and cache.

```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        // Provide the application  context and build the gateway object
        APAndroidGateway gw = builder.build(getActivity().getApplication());
```

Response caching is supported and the cache files are written to disk when enabled.


Setup
===========

This is a setup instruction for an Android Studio project. Unzip the archive and place APGW-[version]-SNAPSHOT>.jar and gw-[version].aar in the libs folder of your application.

Modify your gradle.build with:

```{java}
dependencies {

    compile(name:'gw-debug', ext:'aar')
    compile 'com.google.guava:guava:18.0'
    compile 'com.google.code.gson:gson:2.5'
    compile 'com.squareup.okhttp:okhttp:2.7.4'

}

```

```{java}
repositories {
    flatDir {
        dirs 'libs'
    }
}
```

Enable internet access in your AndroidManfiest.xml

<uses-permission android:name="android.permission.INTERNET"/>


Examples
===========


Basic Usage
===========

Sends a POST synchronously
```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        APAndroidGateway gw = builder.build(getActivity().getApplication());

        gw.setBody("{'foo':'bar'}");

        gw.post("/bar");

        ResponseFromRequest result = gw.readResponse();

        // Access response body
        System.out.println(result.data)

```

Sends a request asynchronously
```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://localhost:3000");

        APAndroidGateway gw = builder.build(getActivity().getApplication());

        gw.get("/api/v1/foo", new APAndroidStringCallback() {

          @Override
          public void finished(String result, Throwable ex) {
              if (ex == null) {
                  System.out.println("Got results: " + result);
              } else {
                  ex.printStackTrace();
              }
          }

        });

```



Certificate Pinning
===========

Certificate pinning allows you to tie certificates against specified domains. It defends against attacks on certificate authorities.
It has it's limitations as well. We require the base64 encoded hashed SubjectPublicKeyInfo. See the example below for google.com

Example:
```{java}
        APAndroidGateway.getCertPinningManager().add("google.com", "sha1/qANMQh2fy6tyyjS9qEjosxOLe1w=");
        APAndroidGateway gw = new APAndroidGateway.Builder().url("https://google.com").build(this.getActivity());

        gw.setUseCertPinning(true);

        APGateway gw = builder.build(getActivity().getApplication());
        gw.post("/bar");
```

Caching
===========

Enable caching of the request

```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://localhost:3000");

        APAndroidGateway gw = builder.build(getActivity().getApplication());

        // Enable caching
        gw.useCaching(true).get("/api/v1/foo", new APAndroidStringCallback() {

          @Override
          public void finished(String result, Throwable ex) {
              if (ex == null) {
                  System.out.println("Got results: " + result);
              } else {
                  ex.printStackTrace();
              }
          }

        });

```

The next request to the same url endpoint will not hit the network.

