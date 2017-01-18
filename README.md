# expect4java
Implementation of expect language for java. Using java 8 closures

 
[ ![Build Status for iTransformers/expect4java](https://codeship.com/projects/430386f0-d1cd-0133-b267-46ddfea9cbb7/status?branch=master)](https://codeship.com/projects/141646)

This library is registers the following Groovy closures into the groovy script's bindings:

send : SendClosure

The send closure is used to send characters.

Example of invoking this closure is: send("say hello\r")

expect : ExpectClosure

The expect closure has several overloads:

expect(string)

Example of invoking this closure overload is: expect("login:")

expect(string, closure)

Example of invoking this closure overload is:

expect("login:") {
 // the closure code invoked if there is a match
}
expect(Match[] mathes)

Example of invoking this closure overload is:

expect ([
_gl("hello\r"){
    println("Matched hello")
    it.exp_continue()
},
_re("hello [0-9]+\r"){ net.itransformers.expect4java.ExpectContext expectState ->
    println("Matched: "+expectState.getMatch())
    expectState.exp_continue()
}
])
_re : RegExpMatchClosure

Match closure object used for matching characters received into input stream using regular expression pattern.

This Match closure can be used as an array element of parameter of expect closure.

The _re closure has two overloads:

_re(string_re_pattern)
_re(string_re_pattern,closure)
The second one has a closure parameter which will be invoked if the regexp matches.

_gl : GlobMatchClosure

Match closure object used for matching characters received into input stream using glob pattern.

This Match closure can be used as an array element of parameter of expect closure.

The _re closure has two overloads:

_gl(string)
_gl(string,closure) The second one has a closure parameter which will be invoked if the regexp matches.
timeout : TimeoutMatchClosure

Match closure object used to handle expect timeouts.

This Match closure can be used as an array element of parameter of expect closure.

For example:

expect ([
   timeout(){
    // some code is executed here if there is timeout
   }
])
or:

expect ([
   timeout(1000L)
])
eof : EofMatchClosure

Inside each match closure the following object is available: net.itransformers.expect4java.ExpectContext.

This object has the following most important methods:

void exp_continue();
void exp_continue_reset_timer();
String getBuffer();
String getMatch(int groupnum);
String getMatch();
Registering groovy closures

The above Groovy closures are registered into script bindings with one of the following overloads of createBindings method:

void Expect4Groovy.createBindings(CLIConnection cliConnection, Binding binding, boolean withLogging);
Map<String, Object> Expect4Groovy.createBindings(CLIConnection cliConnection);
Map<String, Object> Expect4Groovy.createBindings(InputStream is, OutputStream os);
Example:

CLIConnection conn = new RawSocketCLIConnection()
conn.connect(["user":"v","password":"123","address":"localhost:23"])
Expect4Groovy.createBindings(conn, getBinding(), true)
Another available connections are:

net.itransformers.expect4groovy.cliconnection.impl.SshCLIConnection
net.itransformers.expect4groovy.cliconnection.impl.EchoCLIConnection
net.itransformers.expect4groovy.cliconnection.impl.RawSocketCLIConnection
net.itransformers.expect4groovy.cliconnection.impl.TelnetCLIConnection
