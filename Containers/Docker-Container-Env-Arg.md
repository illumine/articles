#Docker Container Env and Arg

The `Dockerfile`:

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

The file `test.txt`

```
  blah blah.....  ${myarg}   
  blah blah.....  ${myenv}
```

The docker compile and run commands:

```

C:\Users\Mike\test>docker build -t mike .
.....

C:\Users\Mike\test>docker run --name cmike  mike
```

The results:
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

Lessons learned:
-Args are used for building the container
-Args can be specified in the `Dockerfile`, using a `docker-compose.yml` file, using an `.env` file, during `docker build` with like
```
docker build --build-arg some_variable_name=a_value
```
-Envs are used to run the container
-Envs are preceeded from Args
-During `COPY` no substitution of Args or Envs takes place.


## Read more
Excellent article:


https://vsupalov.com/docker-env-vars/

