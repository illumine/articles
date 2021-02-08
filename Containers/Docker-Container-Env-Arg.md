# Docker Container Env and Arg


## Preparing the container
The `Dockerfile` that defines some Build Arguments and Environment variables:

```
From alpine:latest

ARG myarg="hi there"
ENV myenv=${myarg}

RUN echo ${myarg}; \
    echo ${myarg} > /argval.txt ;\
	env > /envval.txt


# Following does not work with docker intepreter:	
#RUN cat <<EOF > /eof.txt  \ 
#  blah blah.....  ${myarg}  \ 
#  blah blah.....  ${myenv}   \
#
#EOF  \
	
RUN echo 'All of your\n\
multiline that you ever wanted  \n\
-->${myarg}\n\
-->${myenv}\n\
into a dockerfile\n'\
>> /etc/example.conf	
	
	
COPY test.txt /test.txt


CMD while true; do sleep 15 ; done 
```

The contents of the file `test.txt`, we use the Environment variables and Build Arguments:

```
  blah blah.....  ${myarg}   
  blah blah.....  ${myenv}
```

## Build and Run the container
The docker build  and run commands:

```

C:\Users\Mike\test>docker build -t mike .
.....

C:\Users\Mike\test>docker run --name cmike  mike
```

## The results
In the running container check what is created:
```
C:\Users\mike\test>docker exec -it cmike  cat /argval.txt
hi there

C:\Users\mike\test>docker exec -it cmike  cat /envval.txt
myenv=hi there
SHLVL=1
HOME=/root
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
myarg=hi there
PWD=/

C:\Users\mike\test>docker exec -it cmike  cat /etc/example.conf
All of your\nmultiline that you ever wanted  \n-->${myarg}\n-->${myenv}\ninto a dockerfile\n

C:\Users\mike\test>docker exec -it cmike  cat /test.txt
  blah blah.....  ${myarg}
  blah blah.....  ${myenv}
```

## Lessons learned:
- Build Arguments are used **to build the container**
- Environment variables are used **to run the container**
- Build Arguments can be specified in the `Dockerfile` using `ARG` directive, using a `docker-compose.yml` file,  during `docker build` with 
`docker` command line attribute `--build-arg` like the following example
```
docker build --build-arg some_variable_name=a_value
```
- Environment variables can be used in the `Dockerfile` using `ENV` directive, using a `docker-compose.yml` file, using an `.env` file, 
during `docker build` with 
`--env , -e`		Set environment variables or 
`--env-file`		Read in a file of environment variables
`docker` command line attribute `--build-arg` like the following example illustrates:
```
docker build --build-arg some_variable_name=a_value
```
- Environment variables are preceeded from Build Arguments
- During `COPY` no substitution of Args or Envs takes place in the COPYed file - Docker does not support templating by default!!!


## Read more
Docker command Manual:


https://docs.docker.com/engine/reference/commandline/run/

Excellent article:


https://vsupalov.com/docker-env-vars/

