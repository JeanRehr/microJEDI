
# microJEDI
A school project, emulates a simple cpu.

The old version is not working as intended, keeping it for future reference.

To compile, go to the directory where ./com directory is located, then run:
```
$ javac com/microjedi/*.java
```

To execute the program, run:
```
$ java com/microjedi/Main
```

Due to Java's lack of unsigned support (especially) for bytes, some instructions might not work (namely only compare) as intended on a Java version (when compare receives a byte with a value higher than 128 it underflows).  

Translated text available in the com/microjedi/Text.java file.

![cupj-1](https://user-images.githubusercontent.com/49696706/120048850-ad90c600-bfee-11eb-9cec-6678d16fcf50.png)
![cupj-2](https://user-images.githubusercontent.com/49696706/120048871-c4371d00-bfee-11eb-9ccb-94efcfb282bc.png)
![cupj-3](https://user-images.githubusercontent.com/49696706/120048888-c8633a80-bfee-11eb-8b82-5dcc107c4c01.png)
![cupj-4](https://user-images.githubusercontent.com/49696706/120048890-c9946780-bfee-11eb-8afb-2254afbe0abe.png)
