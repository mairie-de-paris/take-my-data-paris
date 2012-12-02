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

interface IControler {

    /**
     * Process the page
     * 
     * @var Page $page The page 
     */
    public function parseAction(Page $page, User $user);

    /**
     * Parse the URI for the model 
     * 
     * @var URI $uri The URI to parse
     */
    public function parseURI(URI $uri);

    /**
     * Retreive the default page in case of permission rejection
     */
    public function getDefaultPage();
    
    /**
     * Retreive the permission to access this page
     */
    public function getPerms();
}
