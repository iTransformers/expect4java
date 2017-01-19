# expect4java
Implementation of expect language for java. Using java 8 closures

 
[ ![Build Status for iTransformers/expect4java](https://codeship.com/projects/430386f0-d1cd-0133-b267-46ddfea9cbb7/status?branch=master)](https://codeship.com/projects/141646)
# How to use this library
The library is registered in the central Maven repository.
All you need to do is to add the following dependecy into your Maven project
```
<dependencies>
    <dependency>
        <groupId>net.itransformers</groupId>
        <artifactId>expect4java</artifactId>
        <version>1.0.5</version>
    </dependency>
</dependencies>
```

# How to use expect4java
1. Create CLIConnection instance
2. Open the connection 
4. Create Expext4j object using the created CLIConnection
5. Close the Expect4j object
6. Close the CLIConnection

# Create CLIConnection object
Expect4java has the following implemented CLIConnections:
```
net.itransformers.expect4java.cliconnection.impl.CrossPipedCLIConnection
net.itransformers.expect4java.cliconnection.impl.EchoCLIConnection
net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection
net.itransformers.expect4java.cliconnection.impl.RawSocketCLIConnection
net.itransformers.expect4java.cliconnection.impl.SshCLIConnection
net.itransformers.expect4java.cliconnection.impl.TelnetCLIConnection
```

It is possible to implement your own CLIConnection by implementing the following interface:
```
net.itransformers.expect4java.cliconnection.CLIConnection
```

The SshCLIConnection and the TelnetCLIConnection are most commonly used.
The LoggableCLIConnection follows the decorator pattern and add logging functionality to any CLIConnection.
Here is an example:
```
CLIConnection sshConn = new LoggableCLIConnection(
        new SshCLIConnection(),
        msg -> System.out.println(">>> "+msg),
        msg -> System.out.println("<<< "+msg)
);
```
The first parameter of LoggaleCLIConnection is the CLIConnection to be wrapped.
The second and the third parameter are objects of type CLIStreamLogger.

# Open CLIConnection
The SSHClIConnection can be opened in the following way:
```
Map<String, Object> connParams = new HashMap<>();
Properties config = new Properties();
config.put("StrictHostKeyChecking", "no");
config.put("PreferredAuthentications", "keyboard-interactive,password");
connParams.put("username", "guest");
connParams.put("password", "pass123");
connParams.put("address", "vyordanov.tk");
connParams.put("port", 22);
connParams.put("timeout", 1000);
connParams.put("config", config);

sshConn.connect(connParams);
```
It is also possible to set UserInfo class to the connection (See the JSH documentation for more details)
```
UserInfo ui=new MySimpleUserInfo("pass123");
connParams.put("userInfo", ui);
```
# Create Expect4j object
This is quite simple:
```
Expect4j e4j = new Expect4jImpl(sshConn);
```

# Close the Expect4j object
Awlays close the expect4j object. This will stop the internal thread by this object.

# Close the CLIConnection
Awlays close the CLICOnnection, to free os resources.

# Using the Expect4j object
## Sending characters to process input stream
This is done by using Expect4j.send method

Example:
```
e4j.send("say hello\r")
```

## Expecting characters from process output stream
This is done withe Expect4j.expect methods.
The expect method has two overloads.
### The first 'expect' overload is:
```
void expect(Match mathes)
```
Example of invoking this overload is:
```
e4j.expect(new RegExpMatch("hello ([^\n]*)\n", (ExpectContext context) -> {
    System.out.println("Hello " + context.getMatch(1));
    status.setValue(true);
}));
```

### The second 'expect' overload is:
```
void expect(Match[] mathes)
```
Example of invoking this overload is:
```
e4j.expect(new Match[]{
    new RegExpMatch("hello ([^\n]*)\n", (ExpectContext it) -> {
        System.out.println("Hello " + it.getMatch(1));
        firsMatch.setValue(true);
        it.exp_continue();
    }),
    new RegExpMatch("hello2 ([^\n]*)\n", (ExpectContext context2) -> {
        System.out.println("Hello2 " + context2.getMatch(1));
        if (firsMatch.booleanValue()) status.setValue(true);
    })
});
```

## Match class
`net.itransformers.expect4java.matches.Match` is an abstract class used to match character from the process output.
It has several child classes like:
```
net.itransformers.expect4java.matches.RegExpMatch
net.itransformers.expect4java.matches.GlobMatch
net.itransformers.expect4java.matches.EofMatch
net.itransformers.expect4java.matches.TimeoutMatch
```

### `RegExpMatch` class
`Match` object used for matching characters received into input stream using regular expression pattern.
This `Match` object can be used as an array element of parameter of expect method.

The `RegExpMatch` constructor has two overloads:
```
RegExpMatch(String patternStr)
RegExpMatch(String patternStr, Closure closure)
```
The second one has a closure parameter which will be invoked if the regexp matches.

### `GlobMatch` class 
`Match` object used for matching characters received into input stream using glob pattern.

This Match object can be used as an array element of parameter of expect method.

The `Glob` constructor has two overloads:
```
GlobMatch(String pattern
GlobMatch(String pattern, Closure closure)
```
The second one has a closure parameter which will be invoked if the regexp matches.

### `TimeoutMatch` class
Match object used to handle expect timeouts.

This Match object can be used as an array element of parameter of expect method.

For example:
```
e4j.expect(new TimeoutMatch(1000L, it -> {
    // some code is executed here if there is timeout
}));
```
or:
```
e4j.expect(new TimeoutMatch(1000L));
```
### `EofMatch` class

Inside each match closure the following object is available: `net.itransformers.expect4java.ExpectContext`.

This object has the following most important methods:
```
void exp_continue();
void exp_continue_reset_timer();
String getBuffer();
String getMatch(int groupnum);
String getMatch();
Registering groovy closures
```
