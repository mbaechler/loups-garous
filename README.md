# Le Loup Garou et la programmation fonctionnelle

## Build slides

```
$ sbt mdoc
```

## continuously build slides

```
$ sbt 'mdoc --watch'
```

## Organization

Slides sources are located in `slides/mdocs`. The build process generates slides in `slides/docs`.

The project uses https://scalameta.org/mdoc/ to compile and generate the scala snippets and Remark
to parse md and display them as slides.

