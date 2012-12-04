<?php

/**
 * Copyright 2012-2013 Mairie de Paris
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

class URI {

    /**
     * @var array URI elements, cutted at '/' 
     */
    private $parts = array();

    /**
     * @var array Alias for URI elements 
     */
    private $alias = array();

    private function __construct() {
        define('URI_OFFSET', count(explode('/', BASE_ROOT)));
    }

    /**
     * Parse the URI of the current page
     * 
     * @param int $bind_offset Offset for page GET variable in the URI
     * @return URI The parsed URI
     */
    public static function parse() {
        $uri = new URI();

        $uri->parts = explode('/', $_SERVER['REQUEST_URI']);
        $uri->bind('page', 0);
        return ($uri);
    }

    /**
     * Bind a name for URI parameters
     * 
     * @param string $name The name for binding
     * @param int $index The index in the URI table to bind to
     * @return bool true if the binding succed, false otherwise 
     */
    public function bind($name, $index) {
        $index += URI_OFFSET;
        if (isset($this->parts[$index]) && $this->parts[$index] !== '') {
            $this->alias[$name] = $index;
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * MAGIC
     * Allow to easely get bound params from the URI
     * 
     * @param string $property The name of the bound part of the URI
     * @return string The value of the bound part of the URI
     */
    public function __get($property) {
        if (!empty($this->alias[$property])) {
            return ($this->parts[$this->alias[$property]]);
        } else {
            return (null);
        }
    }

    public function __isset($name) {
        if (!empty($this->alias[$name])) {
            return (true);
        } else {
            return (false);
        }
    }

}
