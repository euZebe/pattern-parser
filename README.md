# pattern-parser

## contexte
un petit décorateur qui a comme rôle, au moment d'un parse de données  il prend en paramètre une valeur, et si ça correspond à ce pattern ${TODAY+<Nombre de jours>D} il renvoie la date du jour + ou - le nombre de jours indiqués sinon il renvoi la valeur d'entrée.

## objectif
refactorer le code d'origine suivant:
```
private Object replaceValues(Object initialValue){
    	try {
    		String toReplace= initialValue.toString();
    		if("${TODAY}".equals(initialValue)){
    			return new Date();
    		}
	    	String isDateParam =  toReplace.substring(0,7);
	    	if(StringUtils.isNotBlank(isDateParam) && isDateParam.equals("${TODAY")){
	    		String operation = toReplace.substring(7,8);    		
	    		String paramDays =toReplace.substring(8, toReplace.indexOf("D}"));
	    		Integer days=Integer.parseInt(paramDays);
	    		if(days>0){
		    		if(operation.equals("+")){
		    			return DateUtils.addDays(new Date(), days);
		    		}else if(operation.equals("-")){
		    			return DateUtils.addDays(new Date(), -days);
		    		}
	    		}
	    	}
	    	return toReplace;
    	}catch(Exception e){
    		return initialValue;
    	}
    }
```
