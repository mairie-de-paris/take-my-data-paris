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

class Page {

    /**
     * @var URI The URI
     */
    private $uri = null;

    /**
     * @var string The page name
     */
    private $module = null;

    /**
     * @var array User variables
     */
    private $var = array();

    /**
     * @var string Template name 
     */
    private $template = null;

    /**
     * @var AConroler The controler 
     */
    private $controler = null;

    /**
     * @var IModel The model 
     */
    private $model = null;

    /**
     * @var string The current view
     */
    private $view = null;

    /**
     * @var array The list of Javascript files
     */
    private $js = array();

    /**
     * Try to laod a page (aka : module) from a URI
     * If the page does not exists, load the splash/home page
     * 
     * @param URI $uri Request page URI
     * @param string $default Default page to show if the page does'n exists
     * @param string $prefix Prefix to add to find specific pages (such as admin/)
     */
    public function __construct(URI $uri, $default, $prefix = '') {

        $this->uri = $uri;

        $module = null;

        if (($page_name = $this->validatePageName($uri->page))) {
            if ($this->validateModule($page_name, $prefix)) {
                $module = $page_name;
            }
        }
        if ($module == null) {
            $module = $default;
        }
        $this->module = $module;
        define('MODULE_ROOT', ROOT . $prefix . '/pages/' . $module . '/');
    }

    /**
     * SECURITY
     * Check if the module valid
     * 
     * @param string $module Module name
     */
    private function validateModule($module, $prefix) {
        return (file_exists(ROOT . $prefix . '/pages/' . $module . '/controler.php'));
    }

    /**
     * SECURITY
     * forbid inclusion of non-module pages
     * 
     * @param string $page_name The name of the requested page
     * @return string The name of the page, if valid, or null
     */
    private function validatePageName($page_name) {
        $matches = array();

        if (preg_match('#^[a-z0-9\-_]+$#i', $page_name, $matches)) {
            return ($matches[0]);
        } else {
            return (null);
        }
    }

    /**
     * MAGIC
     * Used by the model to store data
     * 
     * @param string $name The key to store the date
     * @param mixed $value The data
     */
    public function __set($name, $value) {
        $this->var[$name] = $value;
    }

    /**
     * MAGIC
     * Used by the view to get retrieve data
     * 
     * @param string $name The key to retrieve the date
     * @return mixed $data The data
     */
    public function __get($name) {
        if (isset($this->var[$name])) {
            return ($this->var[$name]);
        } else {
            return ($this->uri->$name);
        }
    }

    public function __isset($name) {
        if (isset($this->var[$name])) {
            return (true);
        } else {
            return (isset($this->uri->$name));
        }
    }

    /**
     * Initialize Controler 
     */
    public function load() {
        require_once(MODULE_ROOT . '/controler.php');
        $this->controler = new Controler();
        $this->controler->parseURI($this->uri);
        $this->controler->process($this);
    }

    /**
     * Initialize Model, process data
     */
    public function process() {
        require_once(MODULE_ROOT . '/' . $this->view . '/model.php');
        $this->model = new Model();
        $this->model->process($this);
    }

    /**
     * Render the page 
     */
    public function render() {
        require_once(MODULE_ROOT . '/' . $this->view . '/view.php');
    }

    /**
     * Set the template of the current page
     * 
     * @param string $name The name of the template
     */
    public function setTemplate($name) {
        $this->template = $name;
    }

    /**
     * Get the template of the current page
     * 
     * @return string The name of the template 
     */
    public function getTemplate() {
        return ($this->template);
    }

    /**
     * Get the page request URI
     * @return URI The URI
     */
    public function getURI() {
        return ($this->uri);
    }

    /**
     * Redirect the user to another page
     * 
     * @param string $page The page to redirect to
     */
    public static function redirect($page) {
        header('Location: ' . $page);
        exit;
    }

    /**
     * Set the current view
     * @param string $view The view to set
     */
    public function setView($view) {
        $this->view = $this->validatePageName($view);
    }

    /**
     * Retreive the list of Javascript files
     * @return array The list of Javascript files
     */
    public function getJS() {
        return ($this->js);
    }

    /**
     * Add external javascript in the load list
     * @param sintr $script The script file
     */
    public function loadJS($script) {
        $this->js[] = MODULE_ROOT . '/' . $script;
    }

    /**
     * Get the current page name
     * @return string The page name
     */
    public function getName() {
        return ($this->module);
    }

}
