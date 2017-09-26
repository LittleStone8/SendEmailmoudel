var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var minifycss = require('gulp-minify-css');
var rename = require('gulp-rename');
var copy = require('gulp-copy');
var imagemin = require('gulp-imagemin');
var pngquant = require('imagemin-pngquant');
var filter = require('gulp-filter');
var path = require('path');
var fs = require('fs');
var _ = require('lodash');
var gutil = require('gulp-util');

var BACKUP_SUFFIX = 'source';
var projectSetting = readProjectSetting();

var commonExcept = ['**/node_modules', '**/gulpfile.js', "**/fckeditor", "**/gwt", projectSetting.imagebackuproot]
var generateJsSrc = generateSrcFn(projectSetting.jsroot);
var generateCssSrc = generateSrcFn(projectSetting.cssroot);
var generateImageSrc = generateSrcFn(projectSetting.imageroot);

gulp.task('minjs', ['copyjs'], function() {
    var src = ['**/*.js'];
    var except = ['**/*.{source,min}.js'].concat(commonExcept);
    return gulp.src(generateJsSrc(src).concat(generateJsSrc(except, true)))
        .pipe(uglify()
        	.on('data', f=>console.log(f.path))
        	.on('error', err => console.log(err))
            .on('error', gutil.log)
            )
        .pipe(gulp.dest(f => f.base))
});

gulp.task('mincss', ['copycss'], function() {
    var src = ['**/*.css'];
    var except = ['**/*.{source,min}.css'].concat(commonExcept);
    return gulp.src(generateCssSrc(src).concat(generateCssSrc(except, true)))
        .pipe(minifycss()
        	.on('data', f=>console.log(f.path))
        	.on('error', err => console.log(err))
            .on('error', gutil.log))
        .pipe(gulp.dest(f => f.base))
})

gulp.task('minimage', ['copyimage'], function() {
    var src = ['**/*.{jpg,png,jpeg,gif}'];
    var except = ['**/*.{source,min}.{jpg,png,jpeg,gif}'].concat(commonExcept)
    return gulp.src(generateImageSrc(src).concat(generateImageSrc(except, true)))
        .pipe(filter(function(file) {
            console.log(file.path);
            return true;
        }))
        .pipe(imagemin({
            progressive: true,
            interlaced: true,
            svgoPlugins: [{ removeViewBox: false }],
            use: [pngquant()]
        }).on('data', f=>console.log(f.path))
        	.on('error', err => console.log(err))
            .on('error', gutil.log))
        .pipe(gulp.dest(f => f.base));
});

//backup js、css至.source.js 或.source.css文件//
gulp.task('copycss', function() {
    var src = ['**/*.css'];
    var except = ['**/*.{source,min}.css'].concat(commonExcept);
    var srcArr = generateJsSrc(src).concat(generateJsSrc(except, true));
    if (!isTrue(projectSetting.backupcss)) {
        srcArr = [];
    }
    return backupToSource(srcArr);
});

gulp.task('copyjs', function() {
    var src = ['**/*.js'];
    var except = ['**/*.{source,min}.js'].concat(commonExcept);
    var srcArr = generateJsSrc(src).concat(generateJsSrc(except, true));
    if (!isTrue(projectSetting.backupjs)) {
        srcArr = [];
    }
    return backupToSource(srcArr);
});

gulp.task('copyimage', function() {
    var src = ['**/*.{jpg,png,jpeg,gif}'];
    var srcArr = generateImageSrc(src).concat(generateImageSrc(commonExcept, true));
    if (!isTrue(projectSetting.backupimage)) {
        srcArr = [];
    }
    return gulp.src(srcArr)
        .pipe(gulp.dest(projectSetting.imagebackuproot));
});

gulp.task('default', ['minjs', 'mincss']);
gulp.task('all', ['mincss', 'minjs', 'minimage']);


function readProjectSetting() {
    var json = {};
    try {
        var file = fs.readFileSync('project.json');
        json = JSON.parse(file);
    } catch (err) {
        throw err;
    }
    return json;
}

function generateSrcFn(rootsrc) {
    var rootArr = !Array.isArray(rootsrc) ? [rootsrc] : rootsrc
    return function(arr, isexcept) {
        arr = arr && arr.length ? arr : [''];
        return rootArr.reduce(function(result, cur, idx) {
            return result.concat(arr.map(function(item) {
                return path.join((!!isexcept ? '!' : '') + cur, item)
            }));
        }, []);
    }
}

function backupToSource(src) {
    //过滤已经存在的backup-source源文件
    var existsBackupFilter = filter(function(file) {
        var filepath = file.path,
            ext = path.extname(filepath),
            filename = path.basename(filepath, ext),
            dirname = path.dirname(filepath);
        var backupfilepath = path.resolve(dirname, filename + '.source' + ext);
        return !fs.existsSync(backupfilepath);
    });

    //js,jpg,png,gif,jpeg
    return gulp.src(src)
        .pipe(existsBackupFilter)
        .pipe(rename({ suffix: '.source' }))
        .pipe(gulp.dest(f => f.base));
}

function isTrue(obj) {
    return /true/i.test(obj);
}
