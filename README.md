AP Android JustAPI Gateway
===========

This is an SDK that you will want to use to interface with the AnyPresence's JustAPI technology.

The design is meant to be modular where you can plug in your own JSON parser or rest client.

Dependencies
===========

These dependencies are needed:

compile 'com.google.code.gson:gson:2.5'

compile 'org.apache.commons:commons-lang3:3.4'

compile 'com.google.guava:guava:19.0'

===========
Quick Examples

Sends a POST synchronously
```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://foo.lvh.me:3000/api/v1/foo");

        APAndroidGateway gw = builder.build(getActivity().getApplication());

        APObject obj = new APObject();
        gw.setBody("{'foo':'bar'}");

        gw.post("/bar");

        gw.readResponseObject(obj);

        String id = obj.get("id");

```

Sends a request asynchronously
```{java}
        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://localhost:3000");

        APAndroidGateway gw = builder.build(getActivity().getApplication());

        gw.get("/api/v1/foo", new APCallback<APObject>() {
            @Override
            public void finished(APObject object, Throwable ex) {
                if (ex == null) {
                    System.out.println("success");
                } else {
                    System.out.println("failure");
                    ex.printStackTrace();
                }
            }
        });
```

Plug in your own JSON Parser
```{java}

    class MyParser implements IParser {

        @Override
        public Map<String, String> parseData(String data) {
            // Parse the data
            return null;
        }

    }

    ...

        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("http://localhost:3000");

        APAndroidGateway gw = builder.build(getActivity().getApplication());
        gw.setJsonParser(new MyParser());
```

Use certificate pinning
```{java}
        APAndroidGateway.getInstance().setupCa("myalias", certificateInBytes);

        APAndroidGateway.Builder builder = new APAndroidGateway.Builder();
        builder.url("https://localhost:3000");
        builder.useCertPinning(true);

        APGateway gw = builder.build(getActivity().getApplication());
        gw.post("/bar");
```