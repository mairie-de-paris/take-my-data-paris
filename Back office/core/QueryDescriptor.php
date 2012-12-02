<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
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

final class QueryDescriptor {

    /**
     * @var string The associated table name
     */
    private $table = null;

    /**
     * @var array The columns list with their alias
     */
    private $columns = array();

    /**
     * Standard constructor for QueryDescriptor
     * 
     * @param string $table The table name
     * @param array $columns The columns list with their alias
     */
    public function __construct($table, array $columns) {
        $this->table = $table;
        $this->columns = $columns;
    }

    /**
     * @return string The table name
     */
    public function getTable() {
        return ($this->table);
    }

    /**
     * @return array The columns list
     */
    public function getColumns() {
        return ($this->columns);
    }

}
