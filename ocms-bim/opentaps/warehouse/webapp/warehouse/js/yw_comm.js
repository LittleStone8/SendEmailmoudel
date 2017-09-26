(function(){
	
	 function toQueryObjects(name, value, recursive) {
	        var self = this,
	            objects = [],
	            i, ln;

	        if (_.isArray(value)) {
	            for (i = 0, ln = value.length; i < ln; i++) {
	                if (recursive) {
	                    objects = objects.concat(self(name + '[' + i + ']', value[i], true));
	                }
	                else {
	                    objects.push({
	                        name: name,
	                        value: value[i]
	                    });
	                }
	            }
	        }
	        else if (_.isObject(value)) {
	            for (i in value) {
	                if (value.hasOwnProperty(i)) {
	                    if (recursive) {
	                        objects = objects.concat(self(name + '[' + i + ']', value[i], true));
	                    }
	                    else {
	                        objects.push({
	                            name: name,
	                            value: value[i]
	                        });
	                    }
	                }
	            }
	        }
	        else {
	            objects.push({
	                name: name,
	                value: value
	            });
	        }

	        return objects;
	    }

	
	
	
	function toQueryString(object, recursive) {
        var paramObjects = [],
            params = [],
            i, j, ln, paramObject, value;

        for (i in object) {
            if (object.hasOwnProperty(i)) {
                paramObjects = paramObjects.concat(toQueryObjects(i, object[i], recursive));
            }
        }

        for (j = 0, ln = paramObjects.length; j < ln; j++) {
            paramObject = paramObjects[j];
            value = paramObject.value;
            
            if (_.isEmpty(value)) {
            	value = '';
            }
            else if (_.isDate(value)) {
            	value =value.getTime();
            }

            params.push(encodeURIComponent(paramObject.name) + '=' + encodeURIComponent(String(value)));
        }

        return params.join('&');
    }
	
	
	function fromQueryString(queryString, recursive) {
        var parts = queryString.replace(/^\?/, '').split('&'),
            object = {},
            temp, components, name, value, i, ln,
            part, j, subLn, matchedKeys, matchedName,
            keys, key, nextKey;
        for (i = 0, ln = parts.length; i < ln; i++) {
            part = parts[i];
            if (part.length > 0) {
                components = part.split('=');
                name = decodeURIComponent(components[0]);
                value = (components[1] !== undefined) ? decodeURIComponent(components[1]) : '';
                if (!recursive) {
                    if (object.hasOwnProperty(name)) {
                        if (!_.isArray(object[name])) {
                            object[name] = [object[name]];
                        }

                        object[name].push(value);
                    }
                    else {
                        object[name] = value;
                    }
                }
                else {
                    matchedKeys = name.match(/(\[):?([^\]]*)\]/g);
                    matchedName = name.match(/^([^\[]+)/);

                    //<debug error>
                    if (!matchedName) {
                        throw new Error('query string given, failed parsing name from "' + part + '"');
                    }
                    //</debug>
                    name = matchedName[0];
                    keys = [];
                    if (matchedKeys === null) {
                        object[name] = value;
                        continue;
                    }
                    for (j = 0, subLn = matchedKeys.length; j < subLn; j++) {
                        key = matchedKeys[j];
                        key = (key.length === 2) ? '' : key.substring(1, key.length - 1);
                        keys.push(key);
                    }
                    keys.unshift(name);
                    temp = object;
                    for (j = 0, subLn = keys.length; j < subLn; j++) {
                        key = keys[j];

                        if (j === subLn - 1) {
                            if (_.isArray(temp) && key === '') {
                                temp.push(value);
                            }
                            else {
                                temp[key] = value;
                            }
                        }
                        else {
                            if (temp[key] === undefined || typeof temp[key] === 'string') {
                                nextKey = keys[j+1];

                                temp[key] = (Ext.isNumeric(nextKey) || nextKey === '') ? [] : {};
                            }

                            temp = temp[key];
                        }
                    }
                }
            }
        }
        return object;
    }
	
//	alert(JSON.stringify(fromQueryString("?colors=red&colors=green&colors=blue")));
//	alert(toQueryString(fromQueryString("?colors=red&colors=green&colors=blue")));
	
	var yi_comm = {};
	yi_comm.fromQueryString = fromQueryString;
	yi_comm.toQueryString = toQueryString;
	window.yi_comm = yi_comm;
})();