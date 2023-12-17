# jenkins-shared-library

Shared Library in Jenkins helps to eliminate the common patterns of code and can be used to define the common patterns as functions can can be called from your pipelines.


### Here is the structure of your Jenkins-Shared-Library 

```
        (root)
        +- src                     # Groovy source files
        |   +- org
        |       +- foo
        |           +- Bar.groovy  # for org.foo.Bar class
        +- vars
        |   +- foo.groovy          # for global 'foo' variable
        |   +- foo.txt             # help for 'foo' variable
        +- resources               # resource files (external libraries only)
        |   +- org
        |       +- foo
        |           +- bar.json    # static helper data for org.foo.Bar

```


The `src`` directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.

The `vars` directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline. So if you had a file called vars/log.groovy with a function like def info(message)…​ in it, you can access this function like log.info "hello world" in the Pipeline. You can put as many functions as you like inside this file. Read on below for more examples and options.


>>> Release Strategy : 

```
1) When you push commits to feature branch : Only lintChecks and codeScan 
2) When changes reach main branch over a PR, that means code it stage and then if we will push a tag marking it stable.
3) When we run the job against a TAG, only during that time, I want artifacts should be created and should be pushed to Nexus.

```


>>> What type of obejcts are supposed to stored where ?

```
1) Code    : Plain Text : Version Controlling  : GitHub 
2) Binary  : Artifacts  : Binary Storage [vss] : JFrog / Nexus
```

>>> What is Allow Redploy In Nexus repository ?

```
This ensure once a artifact is published, it will be never be overridden.

```


>>> How are we uploading the artifact now ?
```
1) First we are creating the artifact all the time when running against a TAG!
2) Then attempting to upload the artifact : 
        a) if the artifact is not available, then it's uploading the artifact.
        b) if the artifact already exists, then it's not uploading.

```
>>> Rather than following steps 1 & 2 :
```
1) I'd like to check the artifact first :
        a) If the artifact is available in NEXUS repo, then I don't generate the artifact.
        b) If the artifact is not available in Nexus repo, then I will be generating the artifact and then upload it 
```

### Command to check the availability of the artifact in the nexus :

```
curl http://x.y.p.q:8081/service/rest/repository/browse/catalogue/ | grep catalogue-002
```